package data;

import message.UserMessage;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息记录.
 * <p>
 *     定义了消息记录的格式
 * </p>
 */
public class MessageRecord implements Serializable {

    /**
     * 序列号用类名MD5生成16位hash值
     */
    private static final long serialVersionUID = 0x5c6ef4645425fa6L;

    public MessageRecord(){

    }


    public BigInteger getChatObjectID() {
        return ChatObjectID;
    }

    public void setChatObjectID(BigInteger chatObjectID) {
        ChatObjectID = chatObjectID;
    }

    /**
     * 获取消息内容列表
     *
     * @return
     */
    public CopyOnWriteArrayList<MessageContent> getMessageContents() {
        return messageContents;
    }



    /**
     * 添加用户的消息记录
     *
     * @param message 格式化消息
     */
    public void addMessageRecord(UserMessage message) {
        synchronized (messageContents) {
            if (message != null) {
                MessageContent contentTmp = FormatUserMessage(message);
                if (contentTmp != null)
                    messageContents.add(contentTmp);
            }
        }
    }

    /**
     * 格式化消息
     *
     * @param message
     * @return 返回格式化的消息内容，如果不是聊天消息，则返回空
     */
    public static MessageContent FormatUserMessage(UserMessage message) {
        if (message.getMessageType() == UserMessage.MessageType.Msg_Private) {
            // TODO 私聊记录
            return new MessageContent(message.getSenderID(), null,
                    false, false, message.getContent());
        } else if (message.getMessageType() == UserMessage.MessageType.Msg_Group) {
            // TODO 群聊记录
            return new MessageContent(message.getSenderID(), null,
                    false, false, message.getContent());
        } else {
            return null;
        }
    }

    /**
     * 消息对象ID
     * <p>
     *     这是相对自己的聊天对象的ID，包括私聊用户ID和群聊群组ID，
     *     消息记录以此作为区分
     * </p>
     */
    private BigInteger ChatObjectID;

    /**
     * 消息内容列表
     */
    final private CopyOnWriteArrayList<MessageContent> messageContents = new CopyOnWriteArrayList<>();


}