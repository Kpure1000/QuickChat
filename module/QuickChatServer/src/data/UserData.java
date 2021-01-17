package data;

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
     * @param ID
     */
    public void addFriend(BigInteger ID) {
        friendList.add(ID);
    }

    /**
     * 删除好友
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
     * @param chatObjID 消息对象，一般为群组或接收者
     * @param serverMessage 反馈消息
     */
    public void addMessageRecord(BigInteger chatObjID, ServerMessage serverMessage) {
        CopyOnWriteArrayList<MessageRecord> recordList =
                messageRecordMap.getOrDefault(chatObjID, null);
        //新建消息记录
        MessageRecord newRecord = new MessageRecord(chatObjID);
        newRecord.addMessageRecord(new MessageContain(serverMessage));
        //判断是否存在对这个聊天对象的记录
        if (recordList == null) {
            //不存在，新建一个键
            recordList = new CopyOnWriteArrayList<>();
            messageRecordMap.put(chatObjID, recordList);
        }
        //添加记录
        recordList.add(newRecord);
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
    private final ConcurrentHashMap<BigInteger, CopyOnWriteArrayList<MessageRecord>> messageRecordMap = new ConcurrentHashMap<>();
}
