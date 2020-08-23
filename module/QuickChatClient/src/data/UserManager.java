package data;

import java.math.BigInteger;

/**
 * 用户管理器.
 * <p>
 * 这个类作为登录用户管理器，
 * 管理目前登录的用户的信息、缓存等数据
 * </p>
 */
public class UserManager {
    private static UserManager instance;

    private UserManager(){

    }
    public static UserManager getInstance() {
        return instance;
    }

    public BigInteger getCurrentID() {
        return currentID;
    }

    public void setCurrentID(BigInteger currentID) {
        this.currentID = currentID;
    }

    //////////////////

    BigInteger currentID;



}
