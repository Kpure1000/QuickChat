package message;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 发自服务器的消息
 */
public class ServerMessage implements Serializable {

    private static final long serialVersionUID = 0xad400628389930a7L;

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
        this.feedbackTime = new Date();
    }

    public ServerMessage(MessageType messageType, BigInteger senderID, BigInteger receiverID, String content, Date feedbackTime) {
        this.messageType = messageType;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.feedbackTime = feedbackTime;
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
         * 好友申请反馈
         */
        Fb_FriendRequire,
        /**
         * 在线列表反馈
         */
        Fb_OnlineList,
        /**
         * 强制下线
         */
        Require_ForcedOffLine,

        /**
         * 有人请求传输文件
         */
        Require_SendFile,

        /**
         * 允许传输文件
         */
        Fb_SendFile,

        /**
         * 允许开始接收文件
         */
        Fb_ReceiveFile,

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

    private Date feedbackTime;

    public Date getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(Date feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}
