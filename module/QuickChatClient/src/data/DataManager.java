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
 *     数据包括记录和配置，
 *     单例模式
 * </p>
 */
public class DataManager {
    private static DataManager instance;

    private DataManager(){
        //TODO 临时写入配置
        //writePublic();
        //从文件读取公共配置
        readPublic();
    }

    public static DataManager getInstance(){
        if(instance==null){
            instance=new DataManager();
            return instance;
        }
        return instance;
    }

    /*------------------------------------------------------*/

    public void readPublic(){
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(publicConfigPath)),"UTF-8"));
            String line;
            line = bufferedReader.readLine();
            String jspnstr="";
            while(line != null){
                jspnstr+=line;
                // Debug.Log(line);
                line = bufferedReader.readLine();
            }
            publicConfig = JSON.parseObject(jspnstr, PublicConfig.class);
            //TODO Debug
            if(publicConfig!=null) {
                Debug.Log("读取公共配置：" + publicConfig.toString());
                for (BigInteger item:
                publicConfig.getIdList()) {
                    if(item!=null)Debug.Log(item.toString());
                }
                for (ServerInfo item :
                        publicConfig.getServerInfoList()) {
                    if(item!=null)Debug.Log(item.toString());
                }
            }
            else{
                Debug.LogError("公共配置为空");
            }
            //关闭输入
            bufferedReader.close();
        } catch (IOException e) {
            Debug.LogError("公共配置读取失败");
            e.printStackTrace();
        }
    }

    public void writePublic(){
        try {
            File file = new File(publicConfigPath);
            if(!file.getParentFile().exists()){
                if(!file.getParentFile().mkdir()){
                    Debug.LogError("创建目录失败");
                    return;
                }
            }
            if(!file.exists()){
                file.createNewFile();
            }
            if(file.exists()) {
                //新建一个文件
                CreatePublicConfig(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BigInteger> getIDRecord(){
        return publicConfig.getIdList();
    }

    public ArrayList<ServerInfo> getServerList(){
        return publicConfig.getServerInfoList();
    }

    public void addIDRecord(){

    }

    private void CreatePublicConfig(File file) throws IOException {
        Debug.Log("创建文件: " + publicConfigPath);
        BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(file)
        );
        publicConfig = new PublicConfig();
        publicConfig.addServerList(new ServerInfo("127.0.0.1", 12345));
        publicConfig.addServerList(new ServerInfo("127.0.0.2", 11111));
        publicConfig.addIdList(new BigInteger("1212121212"));
        publicConfig.addIdList(new BigInteger("123123123"));
        //  Debug
        for (BigInteger item:
                publicConfig.getIdList()) {
            if(item!=null)Debug.Log(item.toString());
        }
        for (ServerInfo item :
                publicConfig.getServerInfoList()) {
            if(item!=null)Debug.Log(item.toString());
        }
        ////
        //转为JSON字符串
        var jsonStr = JSON.toJSONString(publicConfig, true);
        // TODO 序列化时，ServerInfo没能序列化
        Debug.Log(jsonStr);
        bufferedWriter.write(jsonStr);
        bufferedWriter.flush();
        //关闭输出
        bufferedWriter.close();
    }

    private PublicConfig publicConfig;

    private String publicConfigPath = "Common/config.json";

    private PrivateConfig privateConfig;

    private String privateConfigPath = "ChatData/config.data";
}
