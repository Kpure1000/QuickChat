package function;

import network.ClientNetwork;

public class BasicFunction {

    /**
     * 关闭功能<p>
     *     在这里释放功能申请的资源
     */
    public void Close(){
        //删除可能订阅的网络回调
        ClientNetwork.getInstance().removeNetCallBack(netCallBack_function);
    }

    /**
     * UI添加网络回调（V-M）
     * @param netCallBack 网络回调
     */
    @Deprecated
    public void addNetCallBack(ClientNetwork.NetCallBack netCallBack){
        ClientNetwork.getInstance().addNetCallBack(netCallBack);
    }

    /**
     * UI删除网络回调（V-M）
     * @param netCallBack 目标回调
     */
    @Deprecated
    public void removeNetCallBack(ClientNetwork.NetCallBack netCallBack){
        ClientNetwork.getInstance().removeNetCallBack(netCallBack);
    }

    /**
     * 本功能可能会用到的一个网络回调
     */
    protected ClientNetwork.NetCallBack netCallBack_function;
}
