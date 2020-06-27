package data;

/**
 * 数据管理器
 * <p>
 *     数据包括记录和配置，
 *     单例模式
 * </p>
 */
public class DataManager {
    private static DataManager instance;

    private DataManager(){}

    public static DataManager getInstance(){
        if(instance==null){
            instance=new DataManager();
            return instance;
        }
        return instance;
    }

    /*------------------------------------------------------*/


}
