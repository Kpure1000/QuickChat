package network;

import function.Debug;
import information.BasicInfo;
import message.UserMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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
     * 添加网络回调
     *
     * @param netCallBack 网络回调
     */
    public synchronized void addNetCallBack(NetCallBack netCallBack) {
        synchronized (netCallBackList) {
            if (netCallBack != null) {
                for (var item :
                        netCallBackList) {
                    if (item == netCallBack) {
                        return;
                    }
                }
                this.netCallBackList.add(netCallBack);
            }
        }
    }

    /**
     * 删除网络回调
     *
     * @param netCallBack 目标回调
     */
    public synchronized void removeNetCallBack(NetCallBack netCallBack) {
        synchronized (netCallBackList) {
            if (netCallBack != null) {
                this.netCallBackList.removeIf(item -> item.equals(netCallBack));
            }
        }
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
            synchronized (netCallBackList) {
                for (var item :
                        netCallBackList) {
                    //连接成功回调
                    item.OnConnectSuccess();
                }
            }
            objOut = new ObjectOutputStream(socket.getOutputStream());
            isConnected = true;
            //创建监听线程
            beginListening(socket);
        } catch (IOException e) {
            synchronized (netCallBackList) {
                for (var item :
                        netCallBackList) {
                    //连接失败回调
                    item.OnConnectFailed();
                }
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
     * 立刻下线，发送下线消息，并断开socket连接，<b>销毁监听线程</b>
     */
    public synchronized void Disconnect() {
        if (socket != null) {
            if (isConnected) {
                try {
                    //发送下线消息
                    sendMessage(new UserMessage(UserMessage.MessageType.Require_Offline,
                            null,null,""));
                    //关闭监听
                    listener.Close();
                    synchronized (netCallBackList) {
                        for (var item :
                                netCallBackList) {
                            //断开连接回调
                            item.OnDisconnect();
                        }
                    }
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
    public void addListenerCallBack(ListenerCallBack listenerCallBack) {
        if (listener == null) {
            Debug.LogError("尚未连接");
            return;
        }
        synchronized (listener) {
            if (listener != null) {
                listener.addListenerCallBack(listenerCallBack);
            }
        }
    }

    /**
     * 删除监听回调
     *
     * @param listenerCallBack 目标回调
     */
    public void removeListenerCallBack(ListenerCallBack listenerCallBack) {
        if (listener == null) {
            Debug.LogError("未连接");
            return;
        }
        synchronized (listener) {
            if (listener != null) {
                listener.removeListenerCallBack(listenerCallBack);
            }
        }
    }

    /**
     * 发送消息
     */
    // TODO param should include 'ClientMessage'
    public void sendMessage(UserMessage userMessage) {
        if (!isConnected) {
            for (NetCallBack item :
                    netCallBackList) {
                item.OnConnectFailed();
            }
            return;
        }
        try {
            if (userMessage == null) {
                System.out.println("消息传入错误");
                return;
            }
            if (objOut == null) // 加载对象输出流
                objOut = new ObjectOutputStream(socket.getOutputStream());
            objOut.writeObject(userMessage);
            objOut.flush();
            synchronized (netCallBackList) {
                for (NetCallBack item :
                        netCallBackList) {
                    item.OnSendMessageSuccess(userMessage);
                }
            }
        } catch (IOException e) {
            // TODO 如果断开连接，broken pipe异常如何处理？
            if (socket.isConnected()) {
                while (!isConnected) {
                    Debug.LogWarning("连接断开，发送失败;正在尝试重新连接...");
                    // TODO 这里其实
                    connect(curHost, curPort);
                    try {
                        //等待400ms
                        Thread.sleep(400);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /*--------------------------------------------*/

    private Socket socket;

    /**
     * 对象输出流
     */
    private ObjectOutputStream objOut;

    private boolean isConnected = false;

    private String curHost;

    private int curPort;

    /**
     * 网络回调列表
     */
    final private CopyOnWriteArrayList<NetCallBack> netCallBackList = new CopyOnWriteArrayList<>();

    /**
     * 网络事件回调定义
     */
    public interface NetCallBack {
        void OnConnectSuccess();

        void OnConnectFailed();

        void OnDisconnect();

        void OnSendMessageSuccess(UserMessage msg);

        void OnSendMessageFailed(UserMessage msg);
    }

    /**
     * 监听
     */
    private Listener listener;

}
