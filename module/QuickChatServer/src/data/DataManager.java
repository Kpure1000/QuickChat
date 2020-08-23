package data;

import function.Debug;

import java.io.*;

public class DataManager {

    private static DataManager instance = new DataManager();

    public static DataManager getInstance() {
        return instance;
    }

    private DataManager() {
        isClosed = true;
    }

    /**
     * 初始化数据（从文件读取）
     */
    public void LoadData() {
        File file = new File(userDataFileName);
        try {
            if (!file.exists()) { //  文件不存在
                Debug.LogWarning("读取时，文件不存在");
                //创建
                writeDefaultUserData();
                //重置
                file = new File(userDataFileName);
            }
            //输入流初始化
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
            userDataContain = (UserDataContain) objIn.readObject();
            userDataContain.initUserDataManager();
            objIn.close();
        } catch (IOException e) {
            Debug.LogError("文件读取错误(EOF)");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.LogWarning("服务器数据反序列化错误");
            //e.printStackTrace();
        }
    }

    /**
     * 写入默认用户数据
     * @throws IOException 输出流异常
     */
    private void writeDefaultUserData() throws IOException {

        File file = new File(userDataFileName);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdir()) {
                Debug.LogError("创建目录失败");
                return;
            }
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        if (file.exists()) {
            //写入
            UserDataContain newUserDataManager = new UserDataContain();
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            objOut.writeObject(newUserDataManager);
            objOut.flush();
            objOut.close();
        } else {
            Debug.LogError("创建文件失败");
        }
    }

    /**
     * 保存数据至文件，关闭之前调用
     */
    private void writeToFile(){
        File file = new File(userDataFileName);
        try {
            if (!file.exists()) { //  文件不存在
                Debug.LogWarning(userDataFileName +" 文件不存在");
                if (!file.getParentFile().exists()) { //  路径不存在
                    if (!file.getParentFile().mkdir()) { //  路径创建失败
                        Debug.LogError("消息记录目录创建失败");
                    }
                    Debug.Log("创建消息记录目录");
                }
                //创建空文件
                file.createNewFile();
            }
            //输出流初始化
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            //输出一个非空记录对象到文件
            objOut.writeObject(userDataContain);
            Debug.Log("保存成功");
            objOut.flush();
            objOut.close();
        } catch(IOException e){
            Debug.LogError("文件写入错误");
            //e.printStackTrace();
        }
    }


    /**
     * 用户数据管理器
     */
    private UserDataContain userDataContain;

    /**
     * 获取用户数据管理器
     * @return
     */
    public UserDataContain getUserDataContain() {
        return userDataContain;
    }

    //Group

    /**
     * 关闭数据管理器，保存数据
     */
    public void Close(){
        writeToFile();
        isClosed=true;
    }

    private boolean isClosed;

    public boolean isClosed() {
        return isClosed;
    }

    public void close() {
        isClosed = true;
    }

    private String userDataFileName = "Common/userData.dat";

    private String groupDataFileName = "Common/groupData.dat";

}
