package function;

import data.DataManager;
import data.ServerInfo;
import message.UserMessage;
import network.ClientNetwork;

import java.util.ArrayList;

/**
 * 欢迎功能
 */
public class Welcome extends BasicFunction {

    private boolean connecting = true;

    public Welcome() {
        Debug.Log("启用欢迎功能...");
        //订阅网络回调
        netCallBack_function = new ClientNetwork.NetCallBack() {
            @Override
            public void OnConnectSuccess() {
                Welcome.this.connecting = false;
                //连接成功
                if (welcomeCallBack != null)
                    welcomeCallBack.OnConnectSuccess();//调用自己的回调，通知UI
            }

            @Override
            public void OnConnectFailed() {
                //继续连接
                Debug.LogWarning("连接尝试失败，继续连接...");
                Welcome.this.connecting = true;
            }

            @Override
            public void OnDisconnect() {
                Debug.Log("已经断开连接");
            }

            @Override
            public void OnSendMessageSuccess(UserMessage msg) {
                Debug.Log("发送消息: " + msg.getMessageType() + ", " + msg.getContent() + " 成功!");
            }

            @Override
            public void OnSendMessageFailed(UserMessage msg) {
                Debug.LogError("发送消息: " + msg.getMessageType() + ", " + msg.getContent() + " 失败!");
            }
        };
        ClientNetwork.getInstance().addNetCallBack(netCallBack_function);
    }

    /**
     * 设置欢迎回调
     *
     * @param welcomeCallBack 欢迎回调
     */
    public void setWelcomeCallBack(WelcomeCallBack welcomeCallBack) {
        this.welcomeCallBack = welcomeCallBack;
        //获取服务器列表，反馈给UI
        welcomeCallBack.OnGetServerList(DataManager.getInstance().getServerList());
    }

    /**
     * 获取服务器列表
     *
     * @return 服务器信息列表
     */
    @Deprecated
    public ArrayList<ServerInfo> getServerList() {
        return DataManager.getInstance().getServerList();
    }

    /**
     * 连接至服务器
     *
     * @param host 主机号
     * @param port 端口
     */
    public void ConnectToServer(String host, int port) {
        //尝试连接服务器
        while (connecting) {
            Debug.Log("尝试连接服务器, info: " + host + ":" + port + "...");
            ClientNetwork.getInstance().connect(host, port);
            try {
                //主线程停顿
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Close() {
        Debug.Log("关闭welcome功能");
        super.Close();
    }

    /**
     * 欢迎功能回调
     */
    public interface WelcomeCallBack {
        void OnGetServerList(ArrayList<ServerInfo> serverList);

        void OnConnectSuccess();
    }

    private WelcomeCallBack welcomeCallBack;

}
