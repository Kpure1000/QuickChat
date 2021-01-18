package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息记录.
 * <p>
 * 定义了消息记录的格式
 * </p>
 */
public class MessageRecord implements Serializable {

    private static final long serialVersionUID = 0x5c6ef4645425fa6L;

    /**
     * @see MessageRecord#ChatObjectID
     * @param chatObjectID
     */
    public MessageRecord(BigInteger chatObjectID) {
        this.ChatObjectID = chatObjectID;
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
     * @return 消息容器列表
     */
    public CopyOnWriteArrayList<MessageContent> getMessageContains() {
        return messageContents;
    }


    /**
     * 添加消息记录
     * @param messageContent 消息容器
     */
    public void addMessageRecord(MessageContent messageContent) {
        synchronized (messageContents) {
            if (messageContent != null) {
                messageContents.add(messageContent);
            }
        }
    }

    public CopyOnWriteArrayList<MessageContent> getMessageContents() {
        return messageContents;
    }

    /**
     * 消息对象ID
     * <p>
     * 这是相对自己的聊天对象的ID，包括私聊用户ID和群聊群组ID，
     * 消息记录以此作为区分；
     * 但如果记录者为群组，则聊天对象为自己
     * </p>
     */
    private BigInteger ChatObjectID;

    /**
     * 消息内容列表
     */
    final private CopyOnWriteArrayList<MessageContent> messageContents = new CopyOnWriteArrayList<>();


}