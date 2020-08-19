package data;

import function.Debug;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;

public class UserDataManager implements Serializable {

    static final long serialVersionUID = 10000001L;

    public UserDataManager() {
        maxID = new BigInteger("10000");
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
     * 获取目前未登录客户端的最大ID，并令该值自增
     * @return ID最大值
     */
    public BigInteger getMaxSignOutClientID() {
        BigInteger tmpID = MaxSignOutClientID;
        //自增
        MaxSignOutClientID = MaxSignOutClientID.add(new BigInteger("1"));
        return tmpID;
    }

    /**
     * 需要序列化的
     */
    private HashMap<BigInteger, UserData> userDataMap = new HashMap<BigInteger, UserData>();

    /**
     * 目前最大的用户ID，用于生成新的用户ID（只需自增）
     */
    private BigInteger maxID;

    /**
     * 这是未登录的客户端
     */
    private BigInteger MaxSignOutClientID = new BigInteger("1");

}
