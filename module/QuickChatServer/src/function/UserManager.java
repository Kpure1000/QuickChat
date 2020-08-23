package function;

import data.UserData;

/**
 * 用户管理器
 */
public class UserManager {
    private static UserManager instance = new UserManager();

    private UserManager(){

    }

    public static UserManager getInstance() {
        return instance;
    }



}
