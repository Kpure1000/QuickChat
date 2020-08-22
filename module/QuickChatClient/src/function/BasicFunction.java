package function;

import network.ClientNetwork;
import network.NetCallBack;

/**
 * 基础功能.
 * <p>
 * 这个类有着所有功能的基础实现，
 * 包括网络回调的订阅与删除，
 * 最重要的方法就是Close，一般需要重写
 * </p>
 */
public class BasicFunction {

    /**
     * 关闭功能.
     * <p>
     * <b>重写的方法中，一定要再次调用super.Close()</b>
     * </p>
     */
    protected void Close() {
        //删除可能订阅的网络回调
        ClientNetwork.getInstance().removeNetCallBack(netCallBack_function);
    }

    /**
     * UI添加网络回调（V-M）
     *
     * @param netCallBack 网络回调
     * @see NetCallBack
     */
    protected void addNetCallBack(NetCallBack netCallBack) {
        ClientNetwork.getInstance().addNetCallBack(netCallBack);
    }

    /**
     * UI删除网络回调（V-M）
     *
     * @param netCallBack 目标回调
     * @see NetCallBack
     */
    protected void removeNetCallBack(NetCallBack netCallBack) {
        ClientNetwork.getInstance().removeNetCallBack(netCallBack);
    }

    /**
     * 本功能可能会用到的一个网络回调
     */
    protected NetCallBack netCallBack_function;
}
