package data;

import message.ServerMessage;
import message.UserMessage;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * 消息容器，对服务器反馈的消息进行封装，便于解释与交流
 */
public class MessageContent implements Serializable {

    private static final long serialVersionUID = 0xeef15e60ded8285dL;

    /**
     * 构造
     *
     * @param senderID 发送者ID
     * @param sendTime 发送时间
     * @param isSent   是否发送
     * @param isRead   是否已读
     * @param content  内容
     */
    public MessageContent(MessageContent.MessageType messageType, BigInteger senderID, BigInteger receiverID, Date sendTime, boolean isSent, boolean isRead, String content) {
        this.messageType = messageType;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.sendTime = sendTime;
        this.isSent = isSent;
        this.isRead = isRead;
        this.content = content;
    }

    public MessageContent(ServerMessage serverMessage) {
        if (serverMessage.getMessageType() == ServerMessage.MessageType.Msg_Group) {
            this.messageType = MessageType.Msg_Group;
        }
        if (serverMessage.getMessageType() == ServerMessage.MessageType.Msg_Private) {
            this.messageType = MessageType.Msg_Private;
        } else {
            this.messageType = MessageType.Msg_Test;
        }
        this.senderID = serverMessage.getSenderID();
        this.receiverID = serverMessage.getReceiverID();
        this.sendTime = serverMessage.getFeedbackTime();
        this.isSent = false;
        this.isRead = false;
        this.content = serverMessage.getContent();
    }

    private MessageContent.MessageType messageType;
    private BigInteger senderID;
    private BigInteger receiverID;
    private Date sendTime;// TODO 这里可能不是用Time
    private boolean isSent;//是否成功发送
    private boolean isRead;//是否已读
    private String content;//内容

    public MessageContent.MessageType getMessageType() {
        return messageType;
    }

    public BigInteger getSenderID() {
        return senderID;
    }

    public BigInteger getReceiverID() {
        return receiverID;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "消息内容{" +
                "发送者ID：" + senderID +
                ", 发送时间：" + sendTime +
                ", 是否发送：" + isSent +
                ", 是否已读：" + isRead +
                ", 内容：'" + content + '\'' +
                "}\r\n";
    }

    public enum MessageType {
        Msg_Private,
        Msg_Group,
        Msg_Test
    }


}
