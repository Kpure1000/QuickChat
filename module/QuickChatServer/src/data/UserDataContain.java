package data;

import function.Debug;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户数据容器
 */
public class UserDataContain implements Serializable {

    static final long serialVersionUID = 0x208b5aebde43a6bcL;

    public UserDataContain() {
        maxID = new BigInteger("10000");
        MaxSignOutClientID = new BigInteger("0");
    }

    /**
     * 重置用户数据
     */
    public void resetUserDataManager() {
        MaxSignOutClientID = new BigInteger("0");
    }

    /**
     * 检查密码
     *
     * @param ID       ID
     * @param password 密码
     * @return 是否正确
     */
    public boolean checkPassword_ID(BigInteger ID, String password) {
        synchronized (userDataMap) {
            if (userDataMap.containsKey(ID)) {
                return userDataMap.get(ID).getPassword().equals(String.valueOf(password));
            }
        }
        return false;
    }

    /**
     * 设置用户密码，如果不存在用户则加入
     *
     * @param ID       ID
     * @param password 新密码
     */
    public void setPassword_ID(BigInteger ID, String password) {
        synchronized (userDataMap) {
            if (userDataMap.containsKey(ID)) {
                userDataMap.get(ID).setPassword(password);
            }
        }
    }

    /**
     * 获取用户信息数据
     * @param ID 目标用户ID
     * @return 用户数据
     */
    public UserData getUserData(BigInteger ID) {
        return userDataMap.getOrDefault(ID, null);
    }

    /**
     * 创建新用户
     *
     * @param name     用户名
     * @param password 用户密码
     * @return 新生成的ID
     */
    public BigInteger CreateNewUser(String name, String password) {
        // 生成新ID
        BigInteger ID = maxID.add(new BigInteger("1"));
        // 自增
        maxID = ID;
        // 加入新用户
        synchronized (userDataMap) {
            userDataMap.put(ID, new UserData(ID, name, "00:00:00", password));
        }
        return ID;
    }

    /**
     * 获取目前未登录的<b>临时</b>客户端的最大ID，并令该值自减
     *
     * @return ID最大值
     */
    public BigInteger getAndAddMaxDefaultID() {
        BigInteger tmpID = MaxSignOutClientID;
        //自增
        MaxSignOutClientID = MaxSignOutClientID.add(new BigInteger("-1"));
        return tmpID;
    }

    /**
     * 用户散列表
     */
    private final ConcurrentHashMap<BigInteger, UserData> userDataMap = new ConcurrentHashMap<>();

    /**
     * 目前最大的用户ID，用于生成新的用户ID（只需自增）
     */
    private BigInteger maxID;

    /**
     * 这是未登录的临时客户端ID
     */
    private BigInteger MaxSignOutClientID;

}
