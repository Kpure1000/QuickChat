package function;

import message.ServerMessage;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;

/**
 * 好友管理功能
 * <p>
 * 这个类负责好友的申请
 *
 * @see function.BasicFunction
 * </p>
 */
public class FriendManager extends BasicFunction {

    public FriendManager() {
        listenerCallBack = new ListenerCallBackAdapter() {
            @Override
            public ListenerCallBack OnListeningStart() {
                return this;
            }

            @Override
            public ListenerCallBack OnForcedOffLine() {
                Debug.Log("被强制下线，关闭聊天窗口");
                Close();
                return this;
            }
        };

    }

    public void inputID(BigInteger ID){
        if(ID!=null){
            Debug.Log("发送申请:请求添加'"+ID.toString()+"'为好友");

            if(listenerCallBack==null){
                listenerCallBack = new ListenerCallBackAdapter() {

                    @Override
                    public ListenerCallBack OnFriendRequireCallBack(ServerMessage serverMessage) {

                        return this;
                    }
                };
            }
            ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);

            ClientNetwork.getInstance().sendMessage(new UserMessage(
                    UserMessage.MessageType.Require_ApplyFriend,
                    // TODO 这里还需要以自己ID作为senderID
                    null,
                    ID,
                    ""
            ));
        }
    }

    /**
     * 关闭自己关联的窗口，释放资源
     *
     * @see BasicFunction#Close()
     */
    @Override
    public void Close() {
        ClientNetwork.getInstance().removeListenerCallBack(listenerCallBack);
        super.Close();
    }

    ListenerCallBack listenerCallBack;

}
