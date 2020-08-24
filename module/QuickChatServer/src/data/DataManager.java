package data;

import function.Debug;

import java.io.*;

public class DataManager {

    private static final DataManager instance = new DataManager();

    public static DataManager getInstance() {
        return instance;
    }

    private DataManager() {
        isClosed = true;
    }

    /**
     * 开始数据管理，加载数据
     */
    public void Start(){
        LoadUserData();
        LoadGroupData();
    }

    @Deprecated
    private void LoadUserData() {
        File file = new File(userDataFileName);
        try {
            if (!file.exists()) { //  文件不存在
                Debug.LogWarning("读取用户数据时，文件不存在");
                //创建
                writeInitializeData(new UserDataContain(), userDataFileName);
                //重置
                file = new File(userDataFileName);
            }
            //输入流初始化
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
            userDataContain = (UserDataContain) objIn.readObject();
            //重置数据
            userDataContain.resetUserDataManager();
            objIn.close();
        } catch (IOException e) {
            Debug.LogError("用户数据文件读取错误(EOF)");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.LogWarning("用户数据反序列化错误");
            //e.printStackTrace();
        }
    }

    @Deprecated
    private void LoadGroupData() {
        File file = new File(groupDataFileName);
        try {
            if (!file.exists()) { //  文件不存在
                Debug.LogWarning("读取群组数据时，文件不存在");
                //创建
                writeInitializeData(new GroupDataContain(), groupDataFileName);
                //重置
                file = new File(groupDataFileName);
            }
            //输入流初始化
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
            groupDataContain = (GroupDataContain) objIn.readObject();
            objIn.close();
        } catch (IOException e) {
            Debug.LogError("群组数据文件读取错误(EOF)");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.LogWarning("群组数据反序列化错误");
            //e.printStackTrace();
        }
    }

    /**
     * 写入默认数据
     *
     * @throws IOException 输出流异常
     */
    private void writeInitializeData(Object data, String fileName) throws IOException {
        File file = new File(fileName);
        CreateFile(file, fileName);
        if (file.exists()) {
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
            objOut.writeObject(data);
            objOut.flush();
            objOut.close();
        } else {
            Debug.LogError("创建文件失败");
        }
    }

    /**
     * 保存用户数据至文件，关闭之前调用
     */
    @Deprecated
    private void writeUserDataToFile() {
        File userFile = new File(userDataFileName);
        try {
            CreateFile(userFile, userDataFileName);
            //输出流初始化
            ObjectOutputStream userDataOut = new ObjectOutputStream(new FileOutputStream(userFile));
            //输出一个非空记录对象到文件
            userDataOut.writeObject(userDataContain);
            Debug.Log("用户数据保存成功");
            userDataOut.flush();
            userDataOut.close();
        } catch (IOException e) {
            Debug.LogError("文件写入错误");
            //e.printStackTrace();
        }
    }

    /**
     * 保存群组数据至文件，关闭之前调用
     */
    @Deprecated
    private void writeGroupDataToFile() {
        File groupFile = new File(groupDataFileName);
        try {
            CreateFile(groupFile, groupDataFileName);
            //输出流初始化
            ObjectOutputStream groupDataOut = new ObjectOutputStream(new FileOutputStream(groupFile));
            //输出一个非空记录对象到文件
            groupDataOut.writeObject(groupDataContain);
            Debug.Log("群组数据保存成功");
            groupDataOut.flush();
            groupDataOut.close();
        } catch (IOException e) {
            Debug.LogError("文件写入错误");
            //e.printStackTrace();
        }
    }

    /**
     * 仅创建文件
     *
     * @param file
     * @param fileName
     */
    private void CreateFile(File file, String fileName) {
        Debug.LogWarning(fileName + " 文件不存在");
        if (!file.getParentFile().exists()) {
            //  路径不存在
            if (!file.exists()) {
                //  文件不存在
                try {
                    file.createNewFile();
                    Debug.Log("创建文件");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!file.getParentFile().mkdir()) {
                //  路径创建失败
                Debug.LogError("数据目录创建失败");
            }
            Debug.Log("创建消息记录目录");
        }
    }

    /**
     * 获取用户数据管理器
     *
     * @return
     */
    public UserDataContain getUserDataContain() {
        return userDataContain;
    }

    public GroupDataContain getGroupDataContain() {
        return groupDataContain;
    }

    /**
     * 关闭数据管理器，保存数据
     */
    public void Close() {
        writeUserDataToFile();
        writeGroupDataToFile();
        isClosed = true;
    }

    /**
     * 用户数据容器
     */
    private UserDataContain userDataContain;

    /**
     * 群组数据容器
     */
    private GroupDataContain groupDataContain;

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
