package network;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务监听线程管理器
 */
public class ServerListenerManager {

    private static ServerListenerManager instance=new ServerListenerManager();

    public static ServerListenerManager getInstance() {
        return instance;
    }

    private ServerListenerManager() {
    }

    /**
     * 加入监听线程
     * @param serverListener
     * @return
     */
    public boolean addServerListener(ServerListener serverListener){
        if(exePool.getActiveCount() >= maxListenerNumber){
            return false;
        }
        exePool.execute(serverListener);
        listenerPool.put(serverListener.getID(),serverListener);
        return true;
    }

    /**
     * 最多允许监听数目（也就是最大在线用户数）
     */
    private final int maxListenerNumber = 5;

    /**
     * 最大允许等待监听数目
     */
    private final int maxWaitNumber = 16;

    /**
     * 获取最大监听数目（最大在线用户数目）
     * @return 最大数目
     */
    public int getMaxListenerNumber() {
        return maxListenerNumber;
    }

    /**
     * 执行池
     */
    private ThreadPoolExecutor exePool = new ThreadPoolExecutor(maxListenerNumber,maxWaitNumber,500, TimeUnit.MILLISECONDS
    ,new ArrayBlockingQueue<Runnable>(maxListenerNumber),new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 监听任务池
     */
    private HashMap<BigInteger,ServerListener> listenerPool= new HashMap<BigInteger,ServerListener>();
}
