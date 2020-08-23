package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用户管理器
 */
public class UserCache extends UserData{

    /**
     * 创建一个在线用户缓存
     * @param userData 用户信息数据
     */
    public UserCache(UserData userData){
        super(userData.ID, userData.Name, userData.createTime, userData.getPassword());
    }

    /**
     * 是否在线
     */
    private boolean isOnLine;

    /**
     * 下线期间未读消息的队列
     */
    private ConcurrentLinkedQueue<MessageRecord> unReadMessage;

}
