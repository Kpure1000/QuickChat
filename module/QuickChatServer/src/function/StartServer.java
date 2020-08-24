package function;

import data.DataManager;
import message.ServerMessage;
import network.*;

import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 开始服务-功能
 */
public class StartServer {
    public StartServer() {
        // 流程
        // TODO加载数据
        DataManager.getInstance().Start();
        // 传入回调订阅，开始接收客户端
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
                // 创建监听任务
                ServerListener newListener = new ServerListener(socket);
                //实现监听回调
                serverListenerCallBack = new ServerListenerCallBackAdapter() {
                    @Override
                    public ServerListenerCallBack OnSendMessageSuccess(ServerMessage message) {
                        Debug.Log("成功地向<" + message.getReceiverID() +
                                ">发送'" + message.getMessageType() + "':" + message.getContent());
                        return super.OnSendMessageSuccess(message);
                    }

                    @Override
                    public ServerListenerCallBack OnSendMessageFailed(ServerMessage message) {
                        Debug.LogError("向" + message.getReceiverID() + "发送消息失败");
                        return super.OnSendMessageFailed(message);
                    }

                    @Override
                    public ServerListenerCallBack OnUserSignIn(BigInteger ID) {
                        Debug.Log("用户: " + ID + " 登录成功");
                        return super.OnUserSignIn(ID);
                    }

                    @Override
                    public ServerListenerCallBack OnUserOffLine(BigInteger ID) {
                        Debug.Log("用户: " + ID + " 下线");
                        return super.OnUserOffLine(ID);
                    }
                };
                // 加入目标监听任务回调
                newListener.addServerListenerCallBack(serverListenerCallBack);
                // 向监听任务管理器加入监听任务
                if (!ServerListenerManager.getInstance().addServerListener(newListener)) {
                    Debug.LogWarning("服务任务暂时已满");
                    // TODO 已满之后执行的策略待定
                }
            }
        });
    }

    public void CloseServer() {
        // TODO 通知所有客户端服务器已关闭
        DataManager.getInstance().Close();
    }

    /**
     * 服务器监听回调
     */
    ServerListenerCallBack serverListenerCallBack;
}
