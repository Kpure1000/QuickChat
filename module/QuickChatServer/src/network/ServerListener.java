package network;

import data.DataManager;
import function.Debug;
import message.ServerMessage;
import message.UserMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        if (socket != null && socket.isConnected()) {
            try {
                objOut = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
                // 反序列化消息
                UserMessage msg = (UserMessage) objIn.readObject();
                synchronized (serverListenerCallBacks) {
                    switch (msg.getMessageType()) {
                        case Check_SignIn_ID -> { //  按照ID登陆
                            String[] msgContent = msg.getContent().split("#");
                            BigInteger idIn = new BigInteger(msgContent[0]);
                            String passIn = msgContent[1];
                            // 根据库检查用户信息
                            // TODO 有可能把'ID不存在'单独列为一种情况
                            if (DataManager.getInstance().getUserDataManager().checkPassword_ID(idIn, passIn)) {
                                // TODO反馈验证通过
                                sendMessage(new ServerMessage(ServerMessage.MessageType.Fb_SignIn,
                                        idIn, null, "pass"));
                            } else {
                                // TODO反馈验证失败
                                sendMessage(new ServerMessage(ServerMessage.MessageType.Fb_SignIn,
                                        idIn, null, "failed"));
                            }
                        }
//                    case Check_SignIn_Email -> {
//                    }
//                    case Check_SignIn_Phone -> {
//                    }
                        case Require_SignUp -> { //  注册
                            String[] msgContent = msg.getContent().split("#");
                            String newName = msgContent[0];
                            String password = msgContent[1];
                            // 创建新用户
                            BigInteger newID = DataManager.getInstance().getUserDataManager().CreateNewUser(newName, password);
                            // TODO 反馈注册信息
                            // 将新生成的ID作为 receiverID 发送
                            sendMessage(new ServerMessage(ServerMessage.MessageType.Fb_SignUp,
                                    null, newID, ""));
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

    private void sendMessage(ServerMessage serverMessage) {
//        if(!isConnected){
//            for (NetCallBack item :
//                    netCallBackList) {
//                item.OnConnectFailed();
//            }
//            return;
//        }
        try {
            if (serverMessage == null) {
                System.out.println("消息传入错误");
                return;
            }
            if (objOut == null) // 加载对象输出流
                objOut = new ObjectOutputStream(socket.getOutputStream());
            objOut.writeObject(serverMessage);
            objOut.flush();
            synchronized (serverListenerCallBacks) {
                for (ServerListenerCallBack item :
                        serverListenerCallBacks) {
                    item.OnSendMessageSuccess(serverMessage);
                }
            }
        } catch (IOException e) {
            // TODO 如果断开连接，broken pipe异常如何处理？
            if (socket.isConnected()) {
                synchronized (serverListenerCallBacks) {
                    for (ServerListenerCallBack item :
                            serverListenerCallBacks) {
                        item.OnSendMessageFailed(serverMessage);
                    }
                }
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

    ObjectOutputStream objOut;

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
        synchronized (serverListenerCallBacks) {
            for (ServerListenerCallBack item :
                    serverListenerCallBacks) {
                if (item.equals(serverListenerCallBack))
                    return;
            }
            serverListenerCallBacks.add(serverListenerCallBack);
        }
    }

    /**
     * 删除监听订阅
     *
     * @param serverListenerCallBack 监听
     */
    public void removeServerListenerCallBack(ServerListenerCallBack serverListenerCallBack) {
        synchronized (serverListenerCallBacks) {
            serverListenerCallBacks.remove(serverListenerCallBack);
        }
    }

}
