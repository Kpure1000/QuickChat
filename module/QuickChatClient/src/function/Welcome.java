package function;

import data.DataManager;
import data.ServerInfo;
import message.UserMessage;
import network.ClientNetwork;
import network.NetCallBack;

import java.util.ArrayList;

/**
 * 欢迎功能
 */
public class Welcome extends BasicFunction {

    private boolean connecting = true;

    public Welcome() {
        //订阅网络回调
        netCallBack_function = new NetCallBack() {
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
                welcomeCallBack.OnConnectFailed();
            }

            @Override
            public void OnDisconnect() {
                Debug.Log("已经断开连接");
            }

            @Override
            public void OnSendMessageSuccess(UserMessage msg) {
            }

            @Override
            public void OnSendMessageFailed(UserMessage msg) {
                Debug.LogError("欢迎: 发送请求: " + msg.getMessageType() + ", " + msg.getContent() + " 失败!");
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
     * 连接至服务器
     *
     * @param host 主机号
     * @param port 端口
     */
    public void ConnectToServer(String host, int port) {
        new Thread(() -> {
            //尝试连接服务器
            while (connecting && retryCount < 5) {
                retryCount++;
                Debug.Log("尝试连接服务器, info: " + host + ":" + port + "...");
                ClientNetwork.getInstance().connect(host, port);
                try {
                    //主线程停顿
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(retryCount>=5) {
                Debug.Log("尝试失败，取消连接");
            }
            retryCount = 0;
        }).start();

    }

    @Override
    public void Close() {
        Debug.Log("关闭welcome功能");
        ClientNetwork.getInstance().removeNetCallBack(netCallBack_function);
        connecting=false;
        super.Close();
    }

    /**
     * 欢迎功能回调
     */
    public interface WelcomeCallBack {
        void OnGetServerList(ArrayList<ServerInfo> serverList);

        void OnConnectSuccess();

        void OnConnectFailed();
    }

    private WelcomeCallBack welcomeCallBack;

    private int retryCount = 0;

}
