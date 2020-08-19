package network;

import function.Debug;
import message.ServerMessage;

import java.math.BigInteger;

/**
 * 服务器监听回调接口
 */
public interface ServerListenerCallBack {
    /**
     * 当成功反馈消息
     * @param message 服务器反馈
     */
    ServerListenerCallBack OnSendMessageSuccess(ServerMessage message);

    /**
     * 当反馈失败
     * @param message 失败的消息
     */
    ServerListenerCallBack OnSendMessageFailed(ServerMessage message);

    /**
     * 当用户成功登录
     * @param ID
     * @return
     */
    ServerListenerCallBack OnUserSignIn(BigInteger ID);
}
