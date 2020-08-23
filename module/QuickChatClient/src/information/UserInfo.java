package information;

/**
 * 用户信息类
 * <p>
 *     继承自基础信息类，有一些扩展信息
 * </p>
 */
public class UserInfo extends BasicInfo {

    private String email;

    private String phoneNumber;

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
