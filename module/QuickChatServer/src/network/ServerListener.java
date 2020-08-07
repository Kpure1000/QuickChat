package network;

import function.Debug;
import message.UserMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener implements Runnable {

    /**
     * 构造一个监听线程
     *
     * @param socket Accept的套接字
     */
    public ServerListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
                // 反序列化消息
                UserMessage msg = (UserMessage) objIn.readObject();
                switch (msg.getMessageType()){
                    case Check_SignIn_ID -> {
                        // TODO 按照ID登陆

                    }
                    case Check_SignIn_Email -> {
                    }
                    case Check_SignIn_Phone -> {
                    }
                    case Require_SignUp -> {
                        // TODO 注册
                    }
                    case Require_Offline -> {
                        // TODO 下线请求
                    }
                    case Require_OnLineList -> {
                        // TODO 在线列表请求
                    }
                    case Require_ApplyFriend -> {
                        // TODO 好友申请
                    }
                    case Require_DeleteFriend -> {
                    }
                    case Require_CreateGroup -> {
                    }
                    case Require_DeleteGroup -> {
                    }
                    case Require_JoinGroup -> {
                    }
                    case Reply_FriendApply -> {
                    }
                    case Reply_GroupApply -> {
                    }
                    case Msg_Private -> {
                    }
                    case Msg_Group -> {
                    }
                    case Msg_Test -> {
                    }
                }
            } catch (IOException e) {
                Debug.LogError("监听服务异常, at: " + ID.toString());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Debug.LogError("反序列化异常, at: " + ID.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务对象ID
     */
    private BigInteger ID;

    /**
     * 获取服务对象ID
     *
     * @return
     */
    public BigInteger getID() {
        return ID;
    }

    /**
     * 服务对象套接字
     */
    private Socket socket;

    /**
     * 监听订阅队列
     */
    private ArrayList<ServerListenerCallBack> serverListenerCallBacks;

    /**
     * 添加监听订阅
     *
     * @param serverListenerCallBack 监听
     */
    public void addServerListenerCallBack(ServerListenerCallBack serverListenerCallBack) {
        for (ServerListenerCallBack item :
                serverListenerCallBacks) {
            if (item.equals(serverListenerCallBack))
                return;
        }
        serverListenerCallBacks.add(serverListenerCallBack);
    }

    /**
     * 删除监听订阅
     *
     * @param serverListenerCallBack 监听
     */
    public void removeServerListenerCallBack(ServerListenerCallBack serverListenerCallBack) {
        serverListenerCallBacks.remove(serverListenerCallBack);
    }

}
