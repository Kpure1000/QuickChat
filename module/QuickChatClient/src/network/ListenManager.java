package network;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 监听线程管理
 */
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
