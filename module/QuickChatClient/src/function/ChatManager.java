package function;

import data.DataManager;
import data.UserManager;
import message.ServerMessage;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;
import java.util.ArrayList;

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
    }

    @Override
    public void Close() {
        Debug.Log("关闭聊天界面");
        DataManager.getInstance().Close();
        ClientNetwork.getInstance().removeListenerCallBack(listenerCallBack);
        super.Close();
        // TODO 还要弄一个ChatManagerCallBack
        ClientNetwork.getInstance().Disconnect();
    }

    public void SelectChatObject(BigInteger id){
        curChatObject = id;
    }

    /**
     * 发送聊天消息
     *
     * @param content
     */
    public void sendMessage(String content) {
        if (curChatObject != null) {
            ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Msg_Test,
                    UserManager.getInstance().getUserInfo().getID(), curChatObject, content));
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
        listenerCallBack = new ListenerCallBackAdapter() {
            @Override
            public ListenerCallBack OnListeningStart() {
                Debug.Log("聊天开始监听");
                return this;
            }

            @Override
            public ListenerCallBack OnReceivePrivateMsg(ServerMessage serverMessage) {
                chatManagerCallBack.OnReceivePrivateMsg(serverMessage);
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveGroupMsg(ServerMessage serverMessage) {
                chatManagerCallBack.OnReceiveGroupMsg(serverMessage);
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveTestMsg(ServerMessage serverMessage) {
                Debug.Log("收到" + serverMessage.getSenderID() + "的测试消息: " + serverMessage.getContent());
                chatManagerCallBack.OnReceiveTestMsg(serverMessage);
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveOnLineList(ServerMessage serverMessage) {
                // TODO 解析列表 id#id#id
                Debug.Log("收到在线列表反馈: " + serverMessage.getContent());
                String userListStr = serverMessage.getContent();
                String[] idStrList = userListStr.split("#");
                ArrayList<BigInteger> idList = new ArrayList<>();
                for (String id :
                        idStrList) {
                    idList.add(new BigInteger(id));
                }
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
        };

        ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);

        requireList();
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
