package data;

import function.Debug;
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

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

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

    /**
     * 添加消息记录，如果不存在该对象则新建一个Record
     *
     * @param chatObjectID
     * @param messageContent
     */
    public void addMessageRecord(BigInteger chatObjectID, MessageContent messageContent) {
        for (var rec :
                messageRecords) {
            if (chatObjectID.compareTo(rec.getChatObjectID()) == 0) {
                //  找到目标对象的记录，直接添加内容
                rec.addMessageRecord(messageContent);

                Debug.Log("存入消息: from" + messageContent.getSenderID()
                        + ", to: " + messageContent.getReceiverID() + ". 总消息数: "
                        + rec.getMessageContents().size());
                return;
            }
        }
        //  不存在该对象，新建一个record，再添加内容
        Debug.Log("不存在该对象记录，因此创建一个");
        MessageRecord newRecord = new MessageRecord(chatObjectID);
        newRecord.addMessageRecord(messageContent);
        messageRecords.add(newRecord);

        Debug.Log("在新记录中存入消息: 给" + messageContent.getSenderID() + ", 总消息数: "
                + newRecord.getMessageContents().size());
    }

    public void setMessageRecords(CopyOnWriteArrayList<MessageRecord> messageRecords) {
        this.messageRecords = messageRecords;
    }

    private UserInfo userInfo;

    /**
     * 消息记录
     */
    private CopyOnWriteArrayList<MessageRecord> messageRecords = new CopyOnWriteArrayList<MessageRecord>();

}
