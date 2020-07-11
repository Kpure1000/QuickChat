package network;

import function.Debug;
import information.BasicInfo;

import java.io.IOException;
import java.net.Socket;

/**
 * clientNetwork
 * <p>
 * 网络层，负责与服务器通讯，确保连接，处理连接异常
 * </p>
 *
 * @author Kpurek
 * @version 1.0
 */
public class ClientNetwork {

    private static ClientNetwork instance = new ClientNetwork();

    public static ClientNetwork getInstance() {
        return instance;
    }

    private ClientNetwork() {
        isConnected = false;
    }


    /**
     * 传入回调实例
     *
     * @param netCallBack
     */
    public void setNetCallBack(NetCallBack netCallBack) {
        this.netCallBack = netCallBack;
    }

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            isConnected = true;
            curHost = host;
            curPort = port;
            netCallBack.OnConnectSuccess();
        } catch (IOException e) {
            isConnected = false;
            netCallBack.OnConnectFailed();
        }
    }

    /**
     * 已连接
     *
     * @return 是否连接
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * 断开当前连接
     */
    public void Disconnect() {

    }

    /**
     * 创建监听线程
     */
    public void addListener(ListenerCallBack listenerCallBack) {
        ListenManager.getInstance().getListenPool().submit(new Listener(socket, listenerCallBack));
    }

    /**
     * 发送消息
     */
    // TODO param should include 'ClientMessage'
    public void sendMessage() {

    }

    /*--------------------------------------------*/

    private Socket socket;

    private boolean isConnected;

    private String curHost;

    private int curPort;

    /**
     * 回调实例
     */
    private NetCallBack netCallBack;

    /**
     * 网络事件回调
     */
    public interface NetCallBack {
        void OnConnectSuccess();

        void OnConnectFailed();

        void OnDisconnect();
    }

}
