package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;

/**
 * 私有配置
 * <p>
 *     用户私有，
 *     用户有读写权
 * </p>
 * @author Kpurek
 * @version 1.0
 */
public class PrivateConfig implements Serializable {

    private static final long serialVersionUID = 1002L;

    /**
     * 是否记住密码
     */
    private boolean isRememberPassword;

    /**
     * 记住的密码
     */
    public String rememberedPassword;

    /**
     * 消息
     */
    public class MessageRecord{
        /**
         * 消息对象（位置）
         */
        private BigInteger ChatObjectID;

        /**
         * 消息内容
         */
        public class MessageContent{
            private BigInteger senderID;
            private Time sendTime;// TODO 这里可能不是用Time
            private boolean isSent;//成功发送
            private boolean isRead;//已读

            private String content;//内容
        }

        /**
         * 消息内容列表
         */
        private ArrayList<MessageContent> messageContents;
    }

    /**
     * 消息列表（对象+内容）
     */
    private ArrayList<MessageRecord> messageRecords;
}
