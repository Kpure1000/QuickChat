package function;

import data.DataManager;
import data.MessageContent;
import data.MessageRecord;
import data.UserManager;
import message.ServerMessage;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;
import network.NetCallBack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

/**
 * 聊天管理功能
 * <p>
 * 这个类是聊天功能的实现类，同时也是聊天主窗口的管理类
 *
 * @see function.BasicFunction
 * @see view.ChatView
 * </p>
 */
public class ChatManager extends BasicFunction {

    public ChatManager() {
        ClientNetwork.getInstance().addNetCallBack(new NetCallBack() {
            @Override
            public void OnConnectSuccess() {

            }

            @Override
            public void OnConnectFailed() {

            }

            @Override
            public void OnDisconnect() {

            }

            @Override
            public void OnSendMessageSuccess(UserMessage msg) {
                if (msg.getMessageType() == UserMessage.MessageType.Msg_Private || msg.getMessageType() == UserMessage.MessageType.Msg_Group
                        || msg.getMessageType() == UserMessage.MessageType.Msg_Test) {
                    //  反馈成功发送了的聊天消息
                    chatManagerCallBack.OnSendMessageSuccess(msg);
                    requireList();
                }
            }

            @Override
            public void OnSendMessageFailed(UserMessage msg) {

            }
        });
    }

    @Override
    public void Close() {
//        Debug.Log("关闭聊天界面");
        DataManager.getInstance().Close();
        ClientNetwork.getInstance().removeListenerCallBack(listenerCallBack);
        super.Close();
        ClientNetwork.getInstance().Disconnect();
    }

    public BigInteger getCurChatObject() {
        return curChatObject;
    }

    public void SelectChatObject(BigInteger id) {
        curChatObject = id;
    }

    /**
     * 发送聊天消息
     *
     * @param content 原始消息内容
     */
    public void sendMessage(String content) {
        //  如果聊天对象不为空
        if (curChatObject != null) {
            if (curChatObject.compareTo(new BigInteger("10000")) > 0) { //  ID 大于10000，是私人用户
                if (curChatObject.compareTo(UserManager.getInstance().getUserInfo().getID()) == 0) { //  发给自己的测试
                    ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Msg_Test,
                            UserManager.getInstance().getUserInfo().getID(), curChatObject, content));
                    //  发送者为自己，直接存入消息记录
                    DataManager.getInstance().addMessageRecord(curChatObject, new MessageContent(
                            MessageContent.MessageType.Msg_Test,
                            UserManager.getInstance().getUserInfo().getID(), curChatObject, new Date(),
                            false, false, content));
                } else { //  私聊消息
                    ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Msg_Private,
                            UserManager.getInstance().getUserInfo().getID(), curChatObject, content));
                    //  发送者为自己，直接存入消息记录
                    DataManager.getInstance().addMessageRecord(curChatObject, new MessageContent(
                            MessageContent.MessageType.Msg_Private,
                            UserManager.getInstance().getUserInfo().getID(), curChatObject, new Date(),
                            false, false, content));
                }
            } else if (curChatObject.compareTo(new BigInteger("9999")) == 0) { //  ID为9999，TODO 目前仅一个广播群
                ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Msg_Group,
                        UserManager.getInstance().getUserInfo().getID(), curChatObject, content));
                //  发送者为自己，直接存入消息记录
                DataManager.getInstance().addMessageRecord(curChatObject, new MessageContent(
                        MessageContent.MessageType.Msg_Group,
                        UserManager.getInstance().getUserInfo().getID(), curChatObject, new Date(),
                        false, false, content));
            }
        }
    }

    /**
     * 请求在线列表
     */
    public void requireList() {
        ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Require_OnLineList,
                UserManager.getInstance().getUserInfo().getID(), null, null));
    }

    public void setChatManagerCallBack(ChatManagerCallBack chatManagerCallBack) {
        this.chatManagerCallBack = chatManagerCallBack;
        ClientNetwork.getInstance().addListenerCallBack(listenerCallBack = new ListenerCallBackAdapter() {
            @Override
            public ListenerCallBack OnListeningStart() {
                return this;
            }

            @Override
            public ListenerCallBack OnReceivePrivateMsg(ServerMessage serverMessage) {
                //  直接存入消息记录
                DataManager.getInstance().addMessageRecord(serverMessage.getSenderID(), new MessageContent(
                        MessageContent.MessageType.Msg_Private,
                        serverMessage.getSenderID(), UserManager.getInstance().getUserInfo().getID(), serverMessage.getFeedbackTime(),
                        false, false, serverMessage.getContent()));
                if (curChatObject != null) chatManagerCallBack.OnReceivePrivateMsg(serverMessage);
                requireList();
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveGroupMsg(ServerMessage serverMessage) {
                //  直接存入消息记录
                DataManager.getInstance().addMessageRecord(serverMessage.getSenderID(), new MessageContent(
                        MessageContent.MessageType.Msg_Group,
                        serverMessage.getSenderID(), UserManager.getInstance().getUserInfo().getID(), serverMessage.getFeedbackTime(),
                        false, false, serverMessage.getContent()));
                if (curChatObject != null) chatManagerCallBack.OnReceivePrivateMsg(serverMessage);
                requireList();
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveTestMsg(ServerMessage serverMessage) {
                //  直接存入消息记录
                DataManager.getInstance().addMessageRecord(serverMessage.getSenderID(), new MessageContent(
                        MessageContent.MessageType.Msg_Test,
                        serverMessage.getSenderID(), UserManager.getInstance().getUserInfo().getID(), serverMessage.getFeedbackTime(),
                        false, false, serverMessage.getContent()));
                if (curChatObject != null) chatManagerCallBack.OnReceivePrivateMsg(serverMessage);
                requireList();
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveOnLineList(ServerMessage serverMessage) {
                String[] idStrList = serverMessage.getContent().split("#");
                ArrayList<BigInteger> idList = new ArrayList<>();
                for (String id :
                        idStrList) {
                    idList.add(new BigInteger(id));
                }
                //  消息列表更新回调
                chatManagerCallBack.OnReceiveOnLineList(idList);
                return this;
            }

            @Override
            public ListenerCallBack OnForcedOffLine() {
                if (chatManagerCallBack != null) {
                    chatManagerCallBack.OnForceClose();
                }
                return this;
            }
        });

    }

    /**
     * 当前聊天对象
     */
    private BigInteger curChatObject;

    /**
     * 监听回调
     */
    private ListenerCallBack listenerCallBack;

    private ChatManagerCallBack chatManagerCallBack;

}
