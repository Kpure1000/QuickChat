package data;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 用户数据类
 */
public class UserData extends BasicData {

    public UserData(BigInteger ID,String name, String createTime,String password) {
        super.ID = ID;
        super.Name = name;
        super.createTime = createTime;
        this.password = password;
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
    private ArrayList<BigInteger> friendList;

    /**
     * 加入的群列表
     */
    private ArrayList<BigInteger> groupJoin;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
