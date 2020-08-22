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
    public void addServerList(ServerInfo serverInfo){
        serverInfoList.add(serverInfo);
    }

    /**
     * 加入ID记录
     * @param id
     */
    public void addIdList(BigInteger id){
        idList.add(id);
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
    private ArrayList<ServerInfo> serverInfoList = new ArrayList<ServerInfo>();

    /** ID列表*/
    private ArrayList<BigInteger> idList = new ArrayList<BigInteger>();
}
