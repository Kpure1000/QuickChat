package data;

import function.Debug;
import message.ServerMessage;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用户数据类
 */
public class UserData extends BasicData {

    public UserData(BigInteger ID, String name, String createTime, String password) {
        super(ID, name);
        super.createTime = createTime;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    //    @Deprecated
    public CopyOnWriteArrayList<BigInteger> getFriendList() {
        return friendList;
    }

    //    @Deprecated
    public CopyOnWriteArrayList<BigInteger> getGroupList() {
        return groupList;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Deprecated
    public void setFriendList(CopyOnWriteArrayList<BigInteger> friendList) {
        this.friendList = friendList;
    }

    @Deprecated
    public void setGroupList(CopyOnWriteArrayList<BigInteger> groupList) {
        this.groupList = groupList;
    }

    /**
     * 是否为好友
     *
     * @param ID 目标用户ID
     * @return
     */
    public boolean isFriend(BigInteger ID) {
        return friendList.contains(ID);
    }

    /**
     * 添加好友
     *
     * @param ID
     */
    public void addFriend(BigInteger ID) {
        friendList.add(ID);
    }

    /**
     * 删除好友
     *
     * @param ID
     */
    public void removeFriend(BigInteger ID) {
        friendList.remove(ID);
    }

    /**
     * 自己是否在群内
     *
     * @param ID 目标群组ID
     * @return
     */
    public boolean isInGroup(BigInteger ID) {
        return groupList.contains(ID);
    }

    /**
     * 加入群聊
     *
     * @param ID
     */
    public void addGroup(BigInteger ID) {
        groupList.add(ID);
    }

    /**
     * 退出群聊
     *
     * @param ID
     */
    public void quitGroup(BigInteger ID) {
        groupList.remove(ID);
    }

    /**
     * 添加消息记录
     *
     * @param chatObjID     消息对象，一般为群组或接收者
     * @param serverMessage 反馈消息
     */
    public void addMessageRecord(BigInteger chatObjectID, MessageContent messageContent) {
        for (var rec :
                messageRecords) {
            if (chatObjectID.compareTo(rec.getChatObjectID()) == 0) {
                //  找到目标对象的记录，直接添加内容
                rec.addMessageRecord(messageContent);

                Debug.Log("存入消息: 给" + messageContent.getSenderID() + ", 总消息数: "
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

    @Override
    public String toString() {
        return super.toString() + "#" + password + "#" + email + "#" + phoneNumber;
    }

    /**
     * 用户登陆密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户邮箱地址
     */
    private String email;

    /**
     * 好友列表
     */
    private CopyOnWriteArrayList<BigInteger> friendList;

    /**
     * 加入的群列表
     */
    private CopyOnWriteArrayList<BigInteger> groupList;

    /**
     * 消息记录
     */
    private final CopyOnWriteArrayList<MessageRecord> messageRecords = new CopyOnWriteArrayList<>();
}
