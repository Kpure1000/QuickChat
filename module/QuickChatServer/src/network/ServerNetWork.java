package network;

import function.Debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetWork {

    private static ServerNetWork instance;

    private ServerNetWork() {
    }

    public static network.ServerNetWork getInstance() {
        if (instance == null) {
            instance = new ServerNetWork();
        }
        return instance;
    }

    ///////////

    /**
     * 首先传入订阅的网络回调，然后开始接收服务
     *
     * @param networkCallBack 网络回调
     */
    public void BeginAccept(NetworkCallBack networkCallBack) {
        this.networkCallBack = networkCallBack;
        // 创建线程单独接收socket
        new Thread(() -> {
            try {
                // 初始化ServerSocket
                serverSocket = new ServerSocket(10808);
                // 成功打开了服务器套接字
                networkCallBack.OnServerOpen(serverSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Debug.Log("开始接收客户端连接...");

            while (true) {
                try {
                    Debug.Log("等待接入的客户端...");
                    Socket newSocket = serverSocket.accept();
                    // TODO 创建监听
                    networkCallBack.OnClientConnect(newSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    ServerSocket serverSocket;

    /**
     * 服务网络回调
     */
    public interface NetworkCallBack {
        /**
         * 当开启服务器
         *
         * @param serverSocket
         */
        void OnServerOpen(ServerSocket serverSocket);

        /**
         * 当有客户端连接
         *
         * @param socket
         */
        void OnClientConnect(Socket socket);
    }

    private NetworkCallBack networkCallBack;

    /**
     * 设置网络回调
     *
     * @param networkCallBack 网络回调
     */
    @Deprecated
    public void setNetworkCallBack(NetworkCallBack networkCallBack) {
        this.networkCallBack = networkCallBack;
    }
}
