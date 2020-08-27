package data;

import information.UserInfo;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private static final long serialVersionUID = 0xa087b9291f7c541bL;

    public PrivateConfig(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public PrivateConfig(BigInteger ID, String Name) {
        userInfo = new UserInfo(ID, Name);
    }

    public PrivateConfig(BigInteger ID, String Name, String password) {
        userInfo = new UserInfo(ID, Name, password);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

//    /**
//     * 设置是否记住密码
//     *
//     * @param remembered
//     */
//    public void setRemembered(boolean remembered) {
//        isRemembered = remembered;
//        if (!isRemembered) rememberedPassword = null;
//    }

//    /**
//     * 是否记住密码
//     *
//     * @return
//     */
//    public boolean isRemembered() {
//        return isRemembered;
//    }

    /**
     * 设置记住的密码
     *
     * @param rememberedPassword 新密码
     */
    public void setRememberedPassword(String rememberedPassword) {
        userInfo.setPassword(rememberedPassword);
//        if (rememberedPassword == null)
//            isRemembered = false;
    }

    /**
     * 获取记住的密码
     *
     * @return 记住的密码
     */
    public String getRememberedPassword() {
//        if (!isRemembered)
//            return null;
        return userInfo.getPassword();
    }

    /**
     * 获取消息记录
     *
     * @return
     */
    public CopyOnWriteArrayList<MessageRecord> getMessageRecords() {
        return messageRecords;
    }

    public void setMessageRecords(CopyOnWriteArrayList<MessageRecord> messageRecords) {
        this.messageRecords = messageRecords;
    }

    private UserInfo userInfo;

//    /**
//     * 是否记住密码
//     */
//    private boolean isRemembered;

//    /**
//     * 记住的密码,不记住的话为null
//     */
//    private String rememberedPassword;

    /**
     * 消息记录
     */
    private CopyOnWriteArrayList<MessageRecord> messageRecords = new CopyOnWriteArrayList<MessageRecord>();

}
