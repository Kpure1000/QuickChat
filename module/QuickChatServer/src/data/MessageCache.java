package data;

import message.ServerMessage;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 消息缓存，仅当消息没有发出去时才放在这里
 */
public class MessageCache implements Serializable {

    private static final long serialVersionUID = 0xb395945de7076e70L;

    private static MessageCache instance = new MessageCache();

    private MessageCache() {
    }

    public static MessageCache getInstance() {
        return instance;
    }

    /**
     * 添加信息
     *
     * @param receiverID    目标ID
     * @param serverMessage 反馈的消息
     */
    public void addMessage(BigInteger receiverID, ServerMessage serverMessage) {
        if (messageCacheMap.containsKey(receiverID)) {
            messageCacheMap.get(receiverID).add(new MessageContain(serverMessage));
        }
    }

    /**
     * 添加消息，给消息内包含的用户
     * @param serverMessage
     */
    public void addMessage(ServerMessage serverMessage) {
        BigInteger id = serverMessage.getReceiverID();
        if (messageCacheMap.containsKey(id)) {
            messageCacheMap.get(id).add(new MessageContain(serverMessage));
        }
    }

    /**
     * 倾倒缓存的未读消息，仅当用户上线时调用
     * @param receiverID 上线的接收者用户
     * @return
     */
    public Queue<MessageContain> pollMessage(BigInteger receiverID) {
        return messageCacheMap.getOrDefault(receiverID, null);
    }

    /**
     * 消息缓存哈希队列
     */
    private final ConcurrentHashMap<BigInteger,
            ConcurrentLinkedQueue<MessageContain>> messageCacheMap = new ConcurrentHashMap<>();
}
