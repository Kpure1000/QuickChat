package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

public class PublicConfig implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * @return
     */
    @Deprecated
    public ArrayList<ServerInfo> getServerInfoList() {
        return serverInfoList;
    }

    /**
     * 获取ID记录
     * @return
     */
    @Deprecated
    public ArrayList<BigInteger> getIdList() {
        return idList;
    }

    /** 服务器列表*/
    private ArrayList<ServerInfo> serverInfoList = new ArrayList<ServerInfo>();

    /** ID列表*/
    private ArrayList<BigInteger> idList = new ArrayList<BigInteger>();
}
