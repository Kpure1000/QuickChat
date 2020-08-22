package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;

/**
 * 消息内容.
 * <p>
 * 这里的消息内容特指经过格式化的消息记录中存储的内容，
 * 并不是发送消息的内容，
 * </p>
 */
public class MessageContent implements Serializable {

    private static final long serialVersionUID = 0x271f85d66fdd0471L;

    /**
     * 构造
     *
     * @param senderID 发送者ID
     * @param sendTime 发送时间
     * @param isSent   是否发送
     * @param isRead   是否已读
     * @param content  内容
     */
    public MessageContent(BigInteger senderID, Time sendTime, boolean isSent, boolean isRead, String content) {
        this.senderID = senderID;
        this.sendTime = sendTime;
        this.isSent = isSent;
        this.isRead = isRead;
        this.content = content;
    }

    private BigInteger senderID;
    private Time sendTime;// TODO 这里可能不是用Time
    private boolean isSent;//是否成功发送
    private boolean isRead;//是否已读
    private String content;//内容

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
}
