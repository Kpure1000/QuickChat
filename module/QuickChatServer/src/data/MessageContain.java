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
public class MessageContain implements Serializable {

    private static final long serialVersionUID = 0xeef15e60ded8285dL;

    /**
     * 构造
     * @param message
     */
    public MessageContain(ServerMessage message) {
        feedbackMessage = message;
        dateTime = Calendar.getInstance().getTime();
    }

    public Date getDateTime() {
        return dateTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    /**
     * 反馈消息-由服务器反馈的
     */
    private ServerMessage feedbackMessage;

    /**
     * 消息反馈的时间
     */
    private Date dateTime;

    /**
     * 是否已读
     */
    private boolean isRead;


}
