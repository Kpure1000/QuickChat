package data;

import java.math.BigInteger;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用户数据类
 */
public class UserData extends BasicData {

    public UserData(BigInteger ID, String name, String createTime, String password) {
        super.ID = ID;
        super.Name = name;
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

    @Deprecated
    public CopyOnWriteArrayList<BigInteger> getFriendList() {
        return friendList;
    }

    @Deprecated
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
     * @param ID 目标用户ID
     * @return
     */
    public boolean isFriend(BigInteger ID) {
        return friendList.contains(ID);
    }

    public void addFriend(BigInteger ID) {
        friendList.add(ID);
    }

    public void removeFriend(BigInteger ID) {
        friendList.remove(ID);
    }

    /**
     * 自己是否在群内
     * @param ID 目标群组ID
     * @return
     */
    public boolean isInGroup(BigInteger ID){
        return groupList.contains(ID);
    }

    public void addGroup(BigInteger ID) {
        groupList.add(ID);
    }

    public void removeGroup(BigInteger ID) {
        groupList.remove(ID);
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
}
