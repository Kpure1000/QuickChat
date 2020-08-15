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
        if (!isRemembered)
            return null;
        return rememberedPassword;
    }

    /**
     * 消息
     */


    /**
     * 消息列表（对象+内容）
     */
    private ArrayList<MessageRecord> messageRecords = new ArrayList<MessageRecord>();

    public ArrayList<MessageRecord> getMessageRecords() {
        return messageRecords;
    }

    public void setMessageRecords(ArrayList<MessageRecord> messageRecords) {
        this.messageRecords = messageRecords;
    }
}
