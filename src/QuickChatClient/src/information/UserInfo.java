package information;

public class UserInfo extends BasicInfo {

    private static final long serialVersionUID = 100101L;

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
