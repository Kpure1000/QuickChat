package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNetWork {

    private static ServerNetWork instance;

    private ServerNetWork(){
        try {
            serverSocket = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static network.ServerNetWork getInstance() {
        if(instance==null){
            instance=new ServerNetWork();
        }
        return instance;
    }

    ////

    /**
     * 开始接收连接
     */
    public void BeginAccept(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();

                networkCallBack.OnClientConnect(newSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ServerSocket serverSocket;

    /**
     * 服务网络回调
     */
    public interface NetworkCallBack{
        /**
         * 当开启服务器
         * @param serverSocket
         */
        void OnServerOpen(ServerSocket serverSocket);

        /**
         * 当有客户端连接
         * @param socket
         */
        void OnClientConnect(Socket socket);
    }

    private NetworkCallBack networkCallBack;

}
