package data;

import java.math.BigInteger;
import java.sql.Time;

/**
 * 消息内容
 */
public class MessageContent {
    /**
     * 构造
     * @param senderID 发送者ID
     * @param sendTime 发送时间
     * @param isSent 是否发送
     * @param isRead 是否已读
     * @param content 内容
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
