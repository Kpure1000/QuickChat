package data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import function.Debug;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;


/**
 * 数据管理器
 * <p>
 * 数据包括记录和配置，
 * 单例模式
 * </p>
 */
public class DataManager {

    //////////////////////////////////

    private static DataManager instance;

    private DataManager() {
        //TODO临时写入配置
        //writePublic();
        //从文件读取公共配置
        readPublic();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
            return instance;
        }
        return instance;
    }

    //////////////////////////////////

    /**
     * 获取公有记录中登录ID历史记录
     *
     * @return BigInteger类型ID列表
     */
    public ArrayList<BigInteger> getIDRecord() {
        return publicConfig.getIdList();
    }

    /**
     * 获取服务器列表
     *
     * @return 服务器信息列表
     */
    public ArrayList<ServerInfo> getServerList() {
        return publicConfig.getServerInfoList();
    }

    /**
     * 获取目标ID的记住密码配置
     *
     * @param ID 目标ID
     * @return 记住的密码，如果不存在ID记录，或该用户选择不记住密码，返回空
     */
    public String getPasswordConfig(BigInteger ID) {
        readPrivate(ID);
        return privateConfig.getRememberedPassword();
    }

    /**
     * 更新记住密码配置
     * @param ID 目标ID
     * @param isRemember 是否记住密码
     */
    public void updatePasswordConfig(BigInteger ID, boolean isRemember){
        readPrivate(ID);
        privateConfig.setRemembered(isRemember);
    }

    /**
     * 更新ID的历史记录，仅当该ID登录成功，否则无意义
     * <p>
     *      不存在则新增登录记录和私有配置，
     *      存在则更新私有配置
     * </p>
     *
     * @param newID          新ID
     * @param passwordConfig 记住密码配置
     */
    public void updateIDRecord(BigInteger newID, String passwordConfig) {
        // TODO 新增历史ID记录，若存在则更新其私人配置
        // 判断是否存在
        for (var item :
                publicConfig.getIdList()) {
            if (item.compareTo(newID) == 0) { //  存在记录
                return;
            }
        }
        // TODO 不存在，添加记录
        publicConfig.addIdList(newID);
    }

    //////////////////////////////////

    /**
     * 读取公有配置
     */
    private void readPublic() {
        try {
            File readerFile = new File(publicConfigPath);
            if(!readerFile.exists()){
                writePublic();
            }
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(readerFile), "UTF-8"));
            String line;
            line = bufferedReader.readLine();
            StringBuilder jsonStr = new StringBuilder();
            while (line != null) {
                jsonStr.append(line);
                // Debug.Log(line);
                line = bufferedReader.readLine();
            }
            publicConfig = JSON.parseObject(jsonStr.toString(), PublicConfig.class);
            if (publicConfig != null) {
                Debug.Log("读取公共配置：" + publicConfig.toString());
                for (BigInteger item :
                        publicConfig.getIdList()) {
                    if (item != null) Debug.Log(item.toString());
                }
                for (ServerInfo item :
                        publicConfig.getServerInfoList()) {
                    if (item != null) Debug.Log(item.toString());
                }
            } else {
                Debug.LogError("公共配置为空");
            }
            //关闭输入
            bufferedReader.close();
        } catch (IOException e) {
            Debug.LogError("公共配置读取失败");
            e.printStackTrace();
        }
    }

    private void updatePublic() {
        try {
            File file = new File(publicConfigPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            // 序列化问题详见 https://github.com/Kpure1000/QuickChat/issues/1
            var jsonStr = JSON.toJSONString(publicConfig, true);
            bufferedWriter.write(jsonStr);
            bufferedWriter.flush();
            //关闭输出
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入公有配置，仅当初次创建时使用
     */
    @Deprecated
    private void writePublic() {
        try {
            File file = new File(publicConfigPath);
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
                //新建一个文件
                CreatePublicConfig(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////

    /**
     * 加载本地私有配置到缓存
     *
     * @param ID 目标ID
     */
    private void readPrivate(BigInteger ID) {
        if (privateConfig != null && privateConfig.getSelfID().compareTo(ID) == 0) {
            //加载的目标配置已经缓存
            return;
        }
        try {
            File file = new File(privateConfigPath + ID.toString() + privateConfigExtend);
            if(!file.exists()){
                initPrivate(ID);
            }
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
            //读取配置到缓存
            privateConfig = (PrivateConfig) objIn.readObject();
            //关闭输入流
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Debug.LogError("读取私有配置时，反序列化出错");
        }
    }

    /**
     * 初次写入私有配置，仅当初次创建私有记录
     *
     * @param ID             新ID
     */
    private void initPrivate(BigInteger ID) {
        try {
            File file = new File(privateConfigPath +
                    ID.toString() + privateConfigExtend);
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
                //新建一个文件
                CreatePrivateConfig(file, ID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新ID配置
     *
     * @param ID             ID
     * @param passwordConfig 记住密码配置
     */
    private void updatePrivate(BigInteger ID, String passwordConfig) {

    }

    /////////////////////////////////////////

    /**
     * 仅当不存在公有配置时，需要创建
     *
     * @param file
     * @throws IOException
     */
    @Deprecated
    private void CreatePublicConfig(File file) throws IOException {
        Debug.Log("创建公有配置文件: " + file.getName());
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(file)
        );
        publicConfig = new PublicConfig();
        publicConfig.addServerList(new ServerInfo("127.0.0.1", 12345));
        publicConfig.addServerList(new ServerInfo("127.0.0.2", 11111));
        publicConfig.addIdList(new BigInteger("1212121212"));
        publicConfig.addIdList(new BigInteger("123123123"));
        //  Debug
        for (BigInteger item :
                publicConfig.getIdList()) {
            if (item != null) Debug.Log(item.toString());
        }
        for (ServerInfo item :
                publicConfig.getServerInfoList()) {
            if (item != null) Debug.Log(item.toString());
        }
        ////
        //转为JSON字符串
        var jsonStr = JSON.toJSONString(publicConfig, true);
        // 序列化问题详见 https://github.com/Kpure1000/QuickChat/issues/1
        Debug.Log(jsonStr);
        bufferedWriter.write(jsonStr);
        bufferedWriter.flush();
        //关闭输出
        bufferedWriter.close();
    }

    /**
     * 按照ID创建私人配置，仅曾经登录成功的才能拥有记录
     *
     * @param file
     * @throws IOException
     */
    private void CreatePrivateConfig(File file, BigInteger ID) throws IOException {
        //初始化私有配置
        privateConfig = new PrivateConfig(ID);
        privateConfig.setRemembered(false);
        //privateConfig.setRememberedPassword();
        //创建文件
        //对象流
        Debug.Log("创建私有配置文件" + file.getName());
        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
        objOut.writeObject(privateConfig);
        objOut.flush();
        //关闭输出
        objOut.close();
    }

    /////////////////////////////////////////

    /**
     * 公有配置缓存
     */
    private PublicConfig publicConfig;
    /**
     * 公有配置路径
     */
    private String publicConfigPath = "Common/config.json";

    /////////////////////////////////////////

    /**
     * 私有配置缓存
     */
    private PrivateConfig privateConfig;
    /**
     * 私有配置路径
     */
    private String privateConfigPath = "Common/";
    /**
     * 私有配置扩展名
     */
    private String privateConfigExtend = ".pri";
}
