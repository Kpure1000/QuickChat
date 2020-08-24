package network;

import data.DataManager;
import data.MessageCache;
import function.Debug;
import message.ServerMessage;
import message.UserMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 服务线程
 */
public class ServerListener implements Runnable {


    /**
     * 构造一个监听线程
     *
     * @param socket Accept的套接字
     */
    public ServerListener(Socket socket) {
        this.serverListenerCallBacks = new CopyOnWriteArrayList<>();
        this.socket = socket;
        //获取未正式登录的默认ID，待正式登录后更新ID
        this.ID = DataManager.getInstance().getUserDataContain().getMaxSignOutClientID();
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
        try {
            objIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Debug.LogWarning("服务获取到的socket输入输出有问题,不能开始监听");
            listening = false;
            e.printStackTrace();
            return;
        }
        while (listening) {
            try {
                // 反序列化消息
                UserMessage msg = (UserMessage) objIn.readObject();
                synchronized (serverListenerCallBacks) {
                    switch (msg.getMessageType()) {
                        case Check_SignIn_ID -> {
                            //  按照ID登陆
                            String[] msgContent = msg.getContent().split("#");
                            BigInteger idIn = new BigInteger(msgContent[0]);
                            String passIn = msgContent[1];
                            // 检查是否已经登录
                            if (ServerListenerManager.getInstance().getServerListener(idIn) != null) {
                                // 反馈 登录成功
                                sendFeedBack(new ServerMessage(ServerMessage.MessageType.Fb_SignIn,
                                        idIn, null, "pass"));
                                // 强制之前登陆的用户下线
                                ServerListenerManager.getInstance().getServerListener(idIn).ForcedOffLine();
                            } else if (DataManager.getInstance().getUserDataContain().checkPassword_ID(idIn, passIn)) {
                                // 根据库检查用户信息
                                // 更新ID
                                ServerListenerManager.getInstance().updateListenerID(ID, idIn);
                                this.ID = idIn;
                                // TODO反馈验证通过
                                ServerMessage message = new ServerMessage(ServerMessage.MessageType.Fb_SignIn,
                                        null, ID, "pass#" +
                                        DataManager.getInstance().getUserDataContain().getUserData(ID).toString()
                                );
                                sendFeedBack(message);
                                // TODO 用户管理器中该用户在线，更新列表

                                for (ServerListenerCallBack item :
                                        serverListenerCallBacks) {
                                    item.OnUserSignIn(idIn);
                                }
                            } else {
                                // TODO反馈验证失败
                                sendFeedBack(new ServerMessage(ServerMessage.MessageType.Fb_SignIn,
                                        null, this.ID, "failed"));
                            }
                        }
//                    case Check_SignIn_Email -> {
//                    }
//                    case Check_SignIn_Phone -> {
//                    }
                        case Require_SignUp -> {
                            //  注册
                            String[] msgContent = msg.getContent().split("#");
                            String newName = msgContent[0];
                            String password = msgContent[1];
                            // 创建新用户
                            BigInteger newID = DataManager.getInstance().getUserDataContain().
                                    CreateNewUser(newName, password);
                            // TODO反馈注册信息
                            // 将新生成的ID作为 receiverID 发送
                            sendFeedBack(new ServerMessage(ServerMessage.MessageType.Fb_SignUp,
                                    null, newID, ""));
                        }
                        case Require_Offline -> {
                            // TODO 下线请求，暂时这么写
                            Close();
                            return;
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
                            sendChatMessage(new ServerMessage(ServerMessage.MessageType.Msg_Private,
                                    msg.getSenderID(), msg.getReceiverID(), msg.getContent()));
                        }
                        case Msg_Group -> {
                            sendChatMessage(new ServerMessage(ServerMessage.MessageType.Msg_Group,
                                    msg.getSenderID(), msg.getReceiverID(), msg.getContent()));
                        }
                        case Msg_Test -> {
                            sendChatMessage(new ServerMessage(ServerMessage.MessageType.Msg_Test,
                                    msg.getSenderID(), msg.getReceiverID(), msg.getContent()));
                        }
                    }
                }
            } catch (SocketException e) {
                Debug.LogError("监听服务Socket异常, at ID: " + ID.toString());
                break;
            } catch (IOException e) {
                Debug.LogError("监听服务输入输出异常, at ID: " + ID.toString());
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                Debug.LogError("反序列化异常, at ID: " + ID.toString());
                //e.printStackTrace();
                break;
            }
        }
    }

    /**
     * 关闭该监听
     */
    public void Close() {
        listening = false;
        synchronized (serverListenerCallBacks) {
            for (ServerListenerCallBack item :
                    serverListenerCallBacks) {
                item.OnUserOffLine(ID);
            }
            serverListenerCallBacks.clear();
        }
        //删除监听
        ServerListenerManager.getInstance().removeListener(this);
        try {
            objIn.close();
            objOut.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送反馈消息，而不是聊天消息
     *
     * @param serverMessage 服务器消息
     */
    private void sendFeedBack(ServerMessage serverMessage) {
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
     * 发送聊天消息，仅当类型为 Msg_* 时使用
     *
     * @param serverMessage 反馈的聊天消息
     */
    private void sendChatMessage(ServerMessage serverMessage) {
        if (serverMessage.getMessageType().equals(ServerMessage.MessageType.Msg_Private)) {
            sendMessage(serverMessage.getReceiverID(), serverMessage);
        } else if (serverMessage.getMessageType().equals(ServerMessage.MessageType.Msg_Group)) {
            BigInteger groupID = serverMessage.getReceiverID();
            // TODO 按照群内成员发送消息
            // 按照群数据遍历成员
        } else if (serverMessage.getMessageType().equals(ServerMessage.MessageType.Msg_Test)) {
            sendMessage(serverMessage.getReceiverID(), serverMessage);
        }
    }

    /**
     * 按照接收者发送消息
     * <p>
     * 之所以要加一个receiverID，是因为如果是群消息，
     * 需要按照群内成员挨个发送消息
     * </p>
     *
     * @param serverMessage
     */
    private void sendMessage(BigInteger receiverID, ServerMessage serverMessage) {
        ServerListener serverListener = ServerListenerManager.getInstance().getServerListener(receiverID);
        if (serverListener != null) {
            // 该用户已上线，TODO 按照消息要求发送
        } else {
            //缓存未读
            MessageCache.getInstance().addMessage(serverMessage.getReceiverID(), serverMessage);
        }
    }

    /**
     * 强制下线
     */
    public void ForcedOffLine() {
        sendFeedBack(new ServerMessage(ServerMessage.MessageType.Require_ForcedOffLine, null, ID, ""));
    }

    /**
     * 获取服务对象ID
     *
     * @return
     */
    public BigInteger getID() {
        return ID;
    }


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

    /**
     * 服务对象ID
     */
    private BigInteger ID;

    /**
     * 是否正在监听
     */
    private boolean listening = true;

    /**
     * 服务对象套接字
     */
    private final Socket socket;

    ObjectOutputStream objOut;

    ObjectInputStream objIn;

    /**
     * 监听订阅队列
     */
    final private CopyOnWriteArrayList<ServerListenerCallBack> serverListenerCallBacks;

}
