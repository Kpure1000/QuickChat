package data;

import information.UserInfo;

import java.math.BigInteger;

/**
 * 用户管理器.
 * <p>
 * 这个类作为登录用户管理器，
 * 管理目前登录的用户的信息、缓存等数据
 * </p>
 */
public class UserManager {
    private static UserManager instance = new UserManager();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return instance;
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
     * 成功登录时，设置用户信息
     *
     * @param ID 目标ID
     */
    public void setUserInfo(BigInteger ID) {
        this.userInfo = DataManager.getInstance().getUserInfoFromConfig(ID);
    }

    public void Close(){
        DataManager.getInstance().updatePrivateConfig(userInfo);
    }

    //////////////////

    UserInfo userInfo;


}
