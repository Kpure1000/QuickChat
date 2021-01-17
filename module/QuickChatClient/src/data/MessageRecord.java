package data;

import message.ServerMessage;
import message.UserMessage;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息记录.
 * <p>
 * 定义了消息记录的格式
 * </p>
 */
public class MessageRecord implements Serializable {

    /**
     * 序列号用类名MD5生成16位hash值
     */
    private static final long serialVersionUID = 0x5c6ef4645425fa6L;

    public MessageRecord(BigInteger chatObjectID) {
        this.ChatObjectID = chatObjectID;
    }

    /**
     * 获取聊天对象的ID
     *
     * @return
     */
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
     * @param content 消息记录内容
     */
    public void addMessageRecord(MessageContent content) {
        synchronized (messageContents) {
            if (content != null) {
                messageContents.add(content);
            }
        }
    }

    /**
     * 消息对象ID
     * <p>
     * 这是相对自己的聊天对象的ID，包括私聊用户ID和群聊群组ID，
     * 消息记录以此作为区分
     * </p>
     */
    private BigInteger ChatObjectID;

    /**
     * 消息内容列表
     */
    final private CopyOnWriteArrayList<MessageContent> messageContents = new CopyOnWriteArrayList<>();


}