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

        listenerCallBack = new ListenerCallBackAdapter() {
            @Override
            public ListenerCallBack OnListeningStart() {
                return super.OnListeningStart();
            }

            @Override
            public ListenerCallBack OnReceivePrivateMsg(ServerMessage serverMessage) {
                return super.OnReceivePrivateMsg(serverMessage);
            }

            @Override
            public ListenerCallBack OnReceiveGroupMsg(ServerMessage serverMessage) {
                return super.OnReceiveGroupMsg(serverMessage);
            }

            @Override
            public ListenerCallBack OnReceiveTestMsg(ServerMessage serverMessage) {
                return super.OnReceiveTestMsg(serverMessage);
            }

            @Override
            public ListenerCallBack OnReceiveOnLineList() {
                return super.OnReceiveOnLineList();
            }

            @Override
            public ListenerCallBack OnForcedOffLine() {
                return super.OnForcedOffLine();
            }
        };

    }

    @Override
    public void Close() {
        DataManager.getInstance().Close();
        ClientNetwork.getInstance().removeListenerCallBack(listenerCallBack);
        super.Close();
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

    /**
     * 当前聊天对象
     */
    private BigInteger curChatObject;

    /**
     * 监听回调
     */
    private ListenerCallBack listenerCallBack;

}
