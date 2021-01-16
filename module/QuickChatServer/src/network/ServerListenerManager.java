package network;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务监听线程管理器
 */
public class ServerListenerManager {

    private static final ServerListenerManager instance = new ServerListenerManager();

    public static ServerListenerManager getInstance() {
        return instance;
    }

    private ServerListenerManager() {
    }

    /**
     * 加入监听线程
     * <p>
     * 注意，新加入的Listener一般没有正式登录的用户ID，
     * 还处于未登录状态，因此Listener在更新ID时候还需要执行
     * {@code updateListenerID}
     *
     * @param serverListener 新加入的任务
     * @return 线程池是否已不适合创建新线程
     * @see ServerListenerManager#updateListenerID(BigInteger, BigInteger)
     * </p>
     */
    public boolean addServerListener(ServerListener serverListener) {
        if (exePool.getActiveCount() >= maxListenerNumber) {
            return false;
        }
        exePool.execute(serverListener);
        synchronized (listenerPool) {
            listenerPool.put(serverListener.getID(), serverListener);
        }
        return true;
    }

    /**
     * 更新监听的ID
     * <p>
     * 当Listener获取到正式登陆的用户ID后，需要将原本的ID更新为新ID
     * （在Listener内调用此方法）
     * </p>
     *
     * @param oldID 旧ID（一般为未登录的默认ID）
     * @param newID 新ID（一般指正式登录的用户ID）
     */
    public void updateListenerID(BigInteger oldID, BigInteger newID) {
        synchronized (listenerPool) {
            if (listenerPool.get(oldID) != null) {//如果存在
                //增加新ID的监听key
                listenerPool.put(newID, listenerPool.get(oldID));
                //删除旧监听的值
                listenerPool.remove(oldID);
            }
        }
    }

    /**
     * 删除监听任务
     *
     * @param serverListener 目标监听，一般是即将结束的Listener
     */
    public void removeListener(ServerListener serverListener) {
        synchronized (listenerPool) {
            synchronized (exePool) {
                exePool.remove(serverListener);
            }
            listenerPool.remove(serverListener.getID());
        }
    }

    /**
     * 按ID索引监听任务
     *
     * @param ID 目标任务所服务的用户的ID
     * @return 目标任务，若不存在或该任务还未绑定ID返回null
     */
    public ServerListener getServerListener(BigInteger ID) {
        synchronized (listenerPool) {
            return listenerPool.get(ID);
        }
    }

    /**
     * 获取最大监听数目（最大在线用户数目）
     *
     * @return 最大数目
     */
    public int getMaxListenerNumber() {
        return maxListenerNumber;
    }

    public ArrayList<BigInteger> getOnLineIDList(){
        return new ArrayList<>(listenerPool.keySet());
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
     * 执行池
     */
    private final ThreadPoolExecutor exePool = new ThreadPoolExecutor(
            maxListenerNumber, maxWaitNumber, 500, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(maxListenerNumber), new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 监听任务池
     * <p>
     * 监听任务按照ID进行索引
     * </p>
     */
    private final HashMap<BigInteger, ServerListener> listenerPool = new HashMap<>();

}
