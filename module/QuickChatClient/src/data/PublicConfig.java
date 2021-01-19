package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class PublicConfig implements Serializable {

    private static final long serialVersionUID = 0x3902486d7511d9adL;

    /**
     * 加入服务器
     * @param serverInfo
     */
    public void addServerInfo(ServerInfo serverInfo){
        serverInfoList.add(serverInfo);
    }

    public void addFileServerInfo(ServerInfo serverInfo){
        fileServerInfoList.add(serverInfo);
    }

    public void setCurrentServer(ServerInfo currentServer) {
        this.currentServer = currentServer;
    }

    /**
     * 获取服务器列表
     * <p>
     *     这个是给FastJson序列化用的
     * </p>
     * @return 服务器列表
     */
    public ArrayList<ServerInfo> getServerInfoList() {
        return serverInfoList;
    }

    public void setCurrentFileServer(ServerInfo currentFileServer) {
        this.currentFileServer = currentFileServer;
    }

    /**
     * 获取文件服务器列表
     * @return 文件服务器列表
     */
    public ArrayList<ServerInfo> getFileServerInfoList() {
        return fileServerInfoList;
    }

    public ServerInfo getCurrentServer() {
        return currentServer;
    }

    public ServerInfo getCurrentFileServer() {
        return currentFileServer;
    }

    /**
     * 获取ID记录
     * <p>
     *     这个是给FastJson序列化用的
     * </p>
     * @return
     */
    public ArrayList<BigInteger> getIdList() {
        return idList;
    }

    /** 服务器列表*/
    private ArrayList<ServerInfo> serverInfoList = new ArrayList<>();

    private ArrayList<ServerInfo> fileServerInfoList = new ArrayList<>();

    private ServerInfo currentServer;

    private ServerInfo currentFileServer;

    /** ID列表*/
    private ArrayList<BigInteger> idList = new ArrayList<BigInteger>();
}
