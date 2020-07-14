package network;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 监听线程管理
 * <p>
 *     废弃这个功能，
 *     为了确保线程安全，减少工作量，
 *     Listener只允许存在一个
 * </p>
 */
@Deprecated
public class ListenManager {
    private ThreadPoolExecutor listenPool = new ThreadPoolExecutor(1, 3,
            300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(32), new ThreadPoolExecutor.DiscardPolicy());

    private static ListenManager instance = new ListenManager();

    private ListenManager() {
    }

    public static ListenManager getInstance() {
        return instance;
    }

    public ThreadPoolExecutor getListenPool() {
        return listenPool;
    }
}
