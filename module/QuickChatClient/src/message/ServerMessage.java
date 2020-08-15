package message;

import java.math.BigInteger;

/**
 * 发自服务器的消息
 */
public class ServerMessage {

    private static final long serialVersionUID = 12345L;

    /**
     * 构造
     *
     * @param messageType 消息类型
     * @param senderID    消息发送者
     * @param receiverID  接收者
     * @param content     消息内容
     */
    public ServerMessage(MessageType messageType, BigInteger senderID, BigInteger receiverID, String content) {
        this.messageType = messageType;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
    }

    public enum MessageType {
        /**
         * 登录反馈
         */
        Fb_SignIn,
        /**
         * 注册反馈
         */
        Fb_SignUp,
        /**
         * 在线列表反馈
         */
        Fb_OnlineList,

        /**
         * 私聊消息
         */
        Msg_Private,
        /**
         * 群聊消息
         */
        Msg_Group,
        /**
         * 测试消息
         */
        Msg_Test
    }

    /**
     * 消息类型
     */
    private MessageType messageType;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * 发送对象ID
     */
    private BigInteger senderID;

    public void setSenderID(BigInteger senderID) {
        this.senderID = senderID;
    }

    public BigInteger getSenderID() {
        return senderID;
    }

    /**
     * 接收对象ID
     */
    private BigInteger receiverID;

    public void setReceiverID(BigInteger receiverID) {
        this.receiverID = receiverID;
    }

    public BigInteger getReceiverID() {
        return receiverID;
    }

    /**
     * 消息内容
     */
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
