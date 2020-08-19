package function;

import data.DataManager;
import network.ServerListener;
import network.ServerNetWork;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 开始服务-功能
 */
public class StartServer {
    public StartServer() {
        // 流程
        // TODO 1.加载数据
        DataManager.getInstance().LoadData();
        // TODO 2.允许连接
        ServerNetWork.getInstance().BeginAccept(new ServerNetWork.NetworkCallBack() {
            @Override
            public void OnServerOpen(ServerSocket serverSocket) {
                // TODO 当成功打开服务套接字
                Debug.Log("成功打开服务器! info:" + serverSocket.getInetAddress() +
                        ":" + serverSocket.getLocalPort());
            }

            @Override
            public void OnClientConnect(Socket socket) {
                // TODO 当有客户端连接
                Debug.Log("客户端: " + socket.getInetAddress() + ":" +
                        socket.getPort() + " 接入服务器!");
            }
        });
    }
}
