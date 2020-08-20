package network;

import message.ServerMessage;

import java.math.BigInteger;

/**
 * 服务监听接口适配器
 */
abstract public class ServerListenerCallBackAdapter implements ServerListenerCallBack{
    @Override
    public ServerListenerCallBack OnSendMessageSuccess(ServerMessage message) {
        return this;
    }

    @Override
    public ServerListenerCallBack OnSendMessageFailed(ServerMessage message) {
        return this;
    }

    @Override
    public ServerListenerCallBack OnUserSignIn(BigInteger ID) {
        return this;
    }

    @Override
    public ServerListenerCallBack OnUserOffLine(BigInteger ID) {
        return this;
    }
}
