package data;

import java.io.Serializable;

/**
 * 服务器信息
 * <p>
 * 这是服务器列表的基础数据结构
 * </p>
 */
public class ServerInfo implements Serializable {

    private static final long serialVersionUID = 0x1ef8e6670c0969abL;

    /**
     * 主机号
     */
    private final String Host;

    /**
     * 端口号
     */
    private final int Port;

    public ServerInfo(String host, int port) {
        Host = host;
        Port = port;
    }


    /**
     * 获取Port
     * <p>
     * 这个是给FastJson序列化用的
     * </p>
     *
     * @return
     */
    public int getPort() {
        return Port;
    }

    /**
     * 获取Host
     * <p>
     * 这个是给FastJson序列化用的
     * </p>
     *
     * @return
     */
    public String getHost() {
        return Host;
    }

    public String toString() {
        return Host + ":" + Port;
    }
}