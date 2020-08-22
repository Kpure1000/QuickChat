package network;

import message.UserMessage;

/**
 * 网络事件回调定义
 */
public interface NetCallBack {
    void OnConnectSuccess();

    void OnConnectFailed();

    void OnDisconnect();

    void OnSendMessageSuccess(UserMessage msg);

    void OnSendMessageFailed(UserMessage msg);
}
