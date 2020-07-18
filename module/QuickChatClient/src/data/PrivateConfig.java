package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;

/**
 * 私有配置，包括用户记住密码选项和消息记录
 * <p>
 * 用户私有，
 * 用户有读写权
 * </p>
 *
 * @author Kpurek
 * @version 1.0
 */
public class PrivateConfig implements Serializable {

    private static final long serialVersionUID = 1002L;

    public PrivateConfig(BigInteger id) {
        selfID = id;
    }

    /**
     * 本配置ID
     */
    private BigInteger selfID;

    /**
     * 获取本配置所属ID
     *
     * @return
     */
    public BigInteger getSelfID() {
        return selfID;
    }

    /**
     * 是否记住密码
     */
    private boolean isRemembered;

    /**
     * 设置是否记住密码
     *
     * @param remembered
     */
    public void setRemembered(boolean remembered) {
        isRemembered = remembered;
    }

    /**
     * 是否记住密码
     *
     * @return
     */
    public boolean isRemembered() {
        return isRemembered;
    }

    /**
     * 记住的密码
     */
    private String rememberedPassword;

    /**
     * 设置记住的密码
     *
     * @param rememberedPassword 新密码
     */
    public void setRememberedPassword(String rememberedPassword) {
        this.rememberedPassword = rememberedPassword;
    }

    /**
     * 获取记住的密码
     *
     * @return 记住的密码
     */
    public String getRememberedPassword() {
        return rememberedPassword;
    }

    /**
     * 消息
     */
    public class MessageRecord {
        /**
         * 消息对象（位置）
         */
        private BigInteger ChatObjectID;

        /**
         * 消息内容
         */
        public class MessageContent {
            private BigInteger senderID;
            private Time sendTime;// TODO 这里可能不是用Time
            private boolean isSent;//是否成功发送
            private boolean isRead;//是否已读
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
