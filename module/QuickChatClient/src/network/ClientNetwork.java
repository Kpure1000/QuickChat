package network;

import function.Debug;
import information.BasicInfo;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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

    //预加载单例
    private static ClientNetwork instance = new ClientNetwork();

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static ClientNetwork getInstance() {
        return instance;
    }

    private ClientNetwork() {
    }

    /**
     * 传入网络回调
     *
     * @param netCallBack 网络回调
     */
    @Deprecated
    public void setNetCallBack(NetCallBack netCallBack) {
        if (netCallBack != null)
            this.netCallBack = netCallBack;
    }

    /**
     * 添加网络回调
     *
     * @param netCallBack 网络回调
     */
    public synchronized void addNetCallBack(NetCallBack netCallBack) {
        if (netCallBack == null) {
            return;
        }
        for (var item :
                netCallBackList) {
            if (item == netCallBack) { //  如果已存在
                return;
            }
        }
        this.netCallBackList.add(netCallBack);
    }

    /**
     * 删除网络回调
     *
     * @param netCallBack 目标回调
     */
    public synchronized void removeNetCallBack(NetCallBack netCallBack) {
        if (netCallBack == null) {
            return;
        }
        this.netCallBackList.removeIf(item -> item.equals(netCallBack));
    }

    /**
     * 连接服务器<p>
     * 如果成功则创建监听线程，
     * 否则取消
     *
     * @param host
     * @param port
     */
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            curHost = host;
            curPort = port;
            //netCallBack.OnConnectSuccess();
            for (var item :
                    netCallBackList) {
                item.OnConnectSuccess();
            }
            isConnected = true;
            //创建监听线程
            beginListening(socket);
        } catch (IOException e) {
            //netCallBack.OnConnectFailed();
            for (var item :
                    netCallBackList) {
                item.OnConnectFailed();
            }
            isConnected = false;
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
        if (socket != null) {
            if (isConnected) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Debug.LogError("Socket关闭失败");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开启监听
     *
     * @param socket 唯一套接字
     */
    private void beginListening(Socket socket) {
        listener = new Listener(socket);
        new Thread(listener).start();
    }

    /**
     * 添加监听回调
     *
     * @param listenerCallBack 监听回调
     */
    public synchronized void addListenerCallBack(ListenerCallBack listenerCallBack) {
        if (listener != null) {
            listener.addListenerCallBack(listenerCallBack);
        }
    }

    /**
     * 删除监听回调
     *
     * @param listenerCallBack 目标回调
     */
    public synchronized void removeListenerCallBacl(ListenerCallBack listenerCallBack) {
        if (listener != null) {
            listener.removeListenerCallBack(listenerCallBack);
        }
    }

    /**
     * 发送消息
     */
    // TODO param should include 'ClientMessage'
    public void sendMessage() {

    }

    /*--------------------------------------------*/

    private Socket socket;

    private boolean isConnected = false;

    private String curHost;

    private int curPort;

    /**
     * 回调实例
     */
    @Deprecated
    private NetCallBack netCallBack;

    /**
     * 网络回调列表
     */
    private ArrayList<NetCallBack> netCallBackList = new ArrayList<NetCallBack>();

    /**
     * 网络事件回调定义
     */
    public interface NetCallBack {
        void OnConnectSuccess();

        void OnConnectFailed();

        void OnDisconnect();
    }

    /**
     * 监听
     */
    private Listener listener;

}
