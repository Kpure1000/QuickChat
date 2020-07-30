package message;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 发自用户的消息
 */
public class UserMessage implements Serializable {

    private static final long serialVersionUID = 1234L;

    public enum MessageType{
        /**按ID登录*/
        Check_SignIn_ID,
        /**按邮箱登录*/
        Check_SignIn_Email,
        /**按手机号登录*/
        Check_SignIn_Phone,

        /**请求注册*/
        Require_SignUp,
        /**请求下线*/
        Require_Offline,
        /**请求在线列表*/
        Require_OnLineList,

        /**请求加好友*/
        Require_ApplyFriend,
        /**请求删好友*/
        Require_DeleteFriend,

        /**请求创建群*/
        Require_CreateGroup,
        /**请求解散群*/
        Require_DeleteGroup,
        /**请求加入群*/
        Require_JoinGroup,


        /**回复好友申请*/
        Reply_FriendApply,

        /**回复加群申请*/
        Reply_GroupApply,

        /**私聊消息*/
        Msg_Private,
        /**群聊消息*/
        Msg_Group,
        /**测试消息*/
        Msg_Test
    }

    /**消息类型*/
    private  MessageType messageType;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**接收对象ID*/
    private BigInteger receiverID;

    public void setReceiverID(BigInteger receiverID) {
        this.receiverID = receiverID;
    }

    public BigInteger getReceiverID() {
        return receiverID;
    }
}
