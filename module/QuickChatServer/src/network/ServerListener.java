package network;

import data.*;
import function.Debug;
import message.ServerMessage;
import message.UserMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
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
        this.ID = DataManager.getInstance().getUserDataContain().getAndAddMaxDefaultID();
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
//                synchronized (serverListenerCallBacks) {
                switch (msg.getMessageType()) {
                    case Check_SignIn_ID -> {
                        //  按照ID登陆
                        String[] msgContent = msg.getContent().split("#");
                        BigInteger idIn = new BigInteger(msgContent[0]);
                        String passIn = msgContent[1];
                        // 检查是否已经登录
                        if (ServerListenerManager.getInstance().getServerListener(idIn) != null) {
                            // 强制之前登陆的用户下线
                            Debug.Log("用户"+idIn+"已经在线，强制下线");
                            ServerListenerManager.getInstance().getServerListener(idIn).ForcedOffLine();
                        }
                        if (DataManager.getInstance().getUserDataContain().checkPassword_ID(idIn, passIn)) {
                            // 根据库检查用户信息
                            // 更新ID
                            ServerListenerManager.getInstance().updateListenerID(ID, idIn);
                            this.ID = idIn;
                            // TODO反馈验证通过
                            ServerMessage message = new ServerMessage(ServerMessage.MessageType.Fb_SignIn,
                                    idIn, ID, "pass#" +
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
                        this.Close();
                        // TODO 下线请求，暂时这么写
                        return;
                    }
                    case Require_OnLineList -> {
                        // TODO 在线列表请求
                        var idList = ServerListenerManager.getInstance().getOnLineIDList();
                        StringBuilder idListStr = new StringBuilder();
                        for (int i = 0; i < idList.size(); i++) {
                            idListStr.append(idList.get(i));
                            idListStr.append('#');
//                            if (i < idList.size() - 1) {
//                                idListStr.append('#');
//                            }
                        }
                        sendFeedBack(new ServerMessage(ServerMessage.MessageType.Fb_OnlineList,
                                null, msg.getSenderID(), idListStr.toString()));
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
//                        Debug.Log("收到来自" + msg.getSenderID() + "的测试消息: " + msg.getContent());
                        sendChatMessage(new ServerMessage(ServerMessage.MessageType.Msg_Test,
                                msg.getSenderID(), msg.getSenderID(), msg.getContent()));
                    }
                }
                DataManager.getInstance().SaveToFile();
//                }
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
//        synchronized (serverListenerCallBacks) {
        for (ServerListenerCallBack item :
                serverListenerCallBacks) {
            item.OnUserOffLine(ID);
        }
        serverListenerCallBacks.clear();
//        }
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
     * 给自己管理的用户反馈消息
     *
     * @param serverMessage 服务器消息
     */
    private void sendFeedBack(ServerMessage serverMessage) {
        sendFeedBack(this, serverMessage);
    }

    /**
     * 给指定用户监听任务 发送消息
     *
     * @param serverListener 用户所在的监听
     * @param serverMessage  反馈的信息
     */
    private boolean sendFeedBack(ServerListener serverListener, ServerMessage serverMessage) {
        try {
            if (serverMessage == null) {
                System.out.println("消息传入错误");
                return false;
            }
            if (objOut == null) // 加载对象输出流
                objOut = new ObjectOutputStream(serverListener.getSocket().getOutputStream());
            objOut.writeObject(serverMessage);
            objOut.flush();
//            synchronized (serverListenerCallBacks) {
            for (ServerListenerCallBack item :
                    serverListenerCallBacks) {
                item.OnSendMessageSuccess(serverMessage);
            }
//            }
            return true;
        } catch (IOException e) {
            if (serverListener.getSocket().isConnected()) {
//                synchronized (serverListenerCallBacks) {
                for (ServerListenerCallBack item :
                        serverListenerCallBacks) {
                    item.OnSendMessageFailed(serverMessage);
                }
//                }
            }
            return false;
        }
    }

    /**
     * 发送聊天消息，仅当类型为 Msg_* 时使用
     *
     * @param serverMessage 反馈的聊天消息
     * @see ServerListener#sendMessage(BigInteger, ServerMessage)
     */
    private void sendChatMessage(ServerMessage serverMessage) {
        if (serverMessage.getMessageType().equals(ServerMessage.MessageType.Msg_Private)) {
            //给私聊对象发送消息
            if (sendMessage(serverMessage.getReceiverID(), serverMessage)) {
                //如果发送成功，保存记录
                DataManager.getInstance().getUserDataContain().
                        getUserData(serverMessage.getSenderID()).//按照发送者ID索引
                        addMessageRecord(serverMessage.getReceiverID(), serverMessage);//消息对象为接收者
            }
        } else if (serverMessage.getMessageType().equals(ServerMessage.MessageType.Msg_Group)) {
            BigInteger groupID = serverMessage.getReceiverID();
            BigInteger senderID = serverMessage.getSenderID();
            GroupDataContain groupDataContain = DataManager.getInstance().getGroupDataContain();
            UserDataContain userDataContain = DataManager.getInstance().getUserDataContain();
            // 按照群数据遍历成员，依次发送消息
            for (BigInteger item :
                    groupDataContain.getMemberList(groupID)) {
                if (sendMessage(item, serverMessage)) {
                    //如果发送成功，保存记录
                    userDataContain.getUserData(senderID).//按照发送者ID索引
                            addMessageRecord(groupID, serverMessage);//消息对象为群组
                }
            }
            //保存群消息记录
            groupDataContain.getGroupData(groupID).
                    addMessageRecord(serverMessage);
        } else if (serverMessage.getMessageType().equals(ServerMessage.MessageType.Msg_Test)) {
            sendMessage(serverMessage.getReceiverID(), serverMessage);
        }
    }

    /**
     * 按照接收者发送消息
     * <p>
     * 如果接收者未上线则缓存消息至缓存器
     *
     * @param serverMessage 反馈消息
     * @see MessageCache
     * </p>
     */
    private boolean sendMessage(BigInteger receiverID, ServerMessage serverMessage) {
        ServerListener serverListener = ServerListenerManager.getInstance().getServerListener(receiverID);
        if (serverListener != null) {
            // 该用户已上线，TODO 按照消息要求发送
            return sendFeedBack(serverListener, serverMessage);
        } else {
            //缓存未读
            MessageCache.getInstance().addMessage(serverMessage.getReceiverID(), serverMessage);
            return false;
        }
    }

    /**
     * 强制下线
     */
    public void ForcedOffLine() {
        sendFeedBack(new ServerMessage(ServerMessage.
                MessageType.Require_ForcedOffLine, null, ID, ""));
    }

    /**
     * 获取服务对象ID
     *
     * @return
     */
    public BigInteger getID() {
        return ID;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * 添加监听订阅
     *
     * @param serverListenerCallBack 监听
     */
    public void addServerListenerCallBack(ServerListenerCallBack serverListenerCallBack) {
//        synchronized (serverListenerCallBacks) {
        for (ServerListenerCallBack item :
                serverListenerCallBacks) {
            if (item.equals(serverListenerCallBack))
                return;
        }
        serverListenerCallBacks.add(serverListenerCallBack);
//        }
    }

    /**
     * 删除监听订阅
     *
     * @param serverListenerCallBack 监听
     */
    public void removeServerListenerCallBack(ServerListenerCallBack serverListenerCallBack) {
//        synchronized (serverListenerCallBacks) {
        serverListenerCallBacks.remove(serverListenerCallBack);
//        }
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
