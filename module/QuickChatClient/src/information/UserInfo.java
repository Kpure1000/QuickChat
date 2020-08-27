package information;

import java.math.BigInteger;

/**
 * 用户信息类
 * <p>
 * 继承自基础信息类，有一些扩展信息
 * </p>
 */
public class UserInfo extends BasicInfo {

    public UserInfo(BigInteger ID, String Name) {
        super(ID, Name);
    }

    public UserInfo(BigInteger ID, String Name, String password) {
        super(ID, Name);
        this.password = password;
    }

    public UserInfo(BigInteger ID, String Name, String createTime, String password, String email, String phoneNumber) {
        super(ID, Name);
        this.setCreateTime(createTime);
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    private String email;

    private String phoneNumber;

    private String password;

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return super.toString() + "#" + password + "#" + email + "#" + phoneNumber;
    }
}
