package data;

import message.ServerMessage;
import message.UserMessage;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息记录.
 * <p>
 *     定义了消息记录的格式
 * </p>
 */
public class MessageRecord implements Serializable {

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
    public CopyOnWriteArrayList<MessageContain> getMessageContents() {
        return messageContains;
    }



    /**
     * 添加用户的消息记录
     *
     * @param message 格式化消息
     */
    public void addMessageRecord(ServerMessage message) {
        synchronized (messageContains) {
            if (message != null) {
                MessageContain contentTmp = FormatUserMessage(message);
                if (contentTmp != null)
                    messageContains.add(contentTmp);
            }
        }
    }

    /**
     * 格式化消息
     *
     * @param message
     * @return 返回格式化的消息内容，如果不是聊天消息，则返回空
     */
    public static MessageContain FormatUserMessage(ServerMessage message) {
        if (message.getMessageType() == ServerMessage.MessageType.Msg_Private) {
            // TODO 私聊记录
            return new MessageContain(message);
        } else if (message.getMessageType() == ServerMessage.MessageType.Msg_Group) {
            // TODO 群聊记录
            return new MessageContain(message);
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
    final private CopyOnWriteArrayList<MessageContain> messageContains = new CopyOnWriteArrayList<>();


}