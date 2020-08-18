package data;

import message.UserMessage;

import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;

public class MessageRecord {
    /**
     * 消息对象（位置）
     */
    private BigInteger ChatObjectID;

    public BigInteger getChatObjectID() {
        return ChatObjectID;
    }

    public void setChatObjectID(BigInteger chatObjectID) {
        ChatObjectID = chatObjectID;
    }

    /**
     * 消息内容列表
     */
    private ArrayList<MessageContent> messageContents = new ArrayList<MessageContent>();

    public ArrayList<MessageContent> getMessageContents() {
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

    @Deprecated
    public void setMessageContents(ArrayList<MessageContent> messageContents) {
        this.messageContents = messageContents;
    }
}