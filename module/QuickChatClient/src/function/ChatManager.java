package function;

import data.DataManager;
import data.UserManager;
import message.ServerMessage;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;

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


    /**
     * 发送消息
     *
     * @param content
     */
    public void sendMessage(String content) {
        ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Msg_Test,
                UserManager.getInstance().getUserInfo().getID(), curChatObject, content));
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
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveGroupMsg(ServerMessage serverMessage) {
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveTestMsg(ServerMessage serverMessage) {
                return this;
            }

            @Override
            public ListenerCallBack OnReceiveOnLineList() {
                return this;
            }

            @Override
            public ListenerCallBack OnForcedOffLine() {
                if(chatManagerCallBack!=null) {
                    chatManagerCallBack.OnForceClose();
                }
                return this;
            }
        };

        ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);
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
