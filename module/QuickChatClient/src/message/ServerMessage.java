package message;

import java.math.BigInteger;

/**
 * 发自服务器的消息
 */
public class ServerMessage {

    private static final long serialVersionUID = 12345l;

    public enum MessageType{
        /**登录反馈*/
        Fb_SignIn,
        /**注册反馈*/
        Fb_SignUp,
        /**在线列表反馈*/
        Fb_OnlineList,

        /**私聊消息*/
        Msg_Private,
        /**群聊消息*/
        Msg_Group,
        /**测试消息*/
        Msg_Test
    }

    /**消息类型*/
    private MessageType messageType;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**发送对象ID*/
    private BigInteger senderID;

    public void setSenderID(BigInteger senderID) {
        this.senderID = senderID;
    }

    public BigInteger getSenderID() {
        return senderID;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
