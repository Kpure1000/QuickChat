package network;

import message.ServerMessage;

import java.math.BigInteger;

/**
 * 监听回调事件的抽象适配器
 *
 * @author Kpurek
 * @version 1.0
 * @see network.ListenerCallBack
 */
public abstract class ListenerCallBackAdapter implements ListenerCallBack {

    public ListenerCallBack OnListeningStart() {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param fbState
     */
    @Override
    public ListenerCallBack OnSignInCallBack(String fbState) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param fbID
     * @return
     */
    @Override
    public ListenerCallBack OnSignUpCallBack(BigInteger fbID) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param serverMessage 反馈申请结果
     * @return
     */
    @Override
    public ListenerCallBack OnFriendRequireCallBack(ServerMessage serverMessage) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param serverMessage
     * @return
     */
    @Override
    public ListenerCallBack OnReceivePrivateMsg(ServerMessage serverMessage) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param serverMessage
     * @return
     */
    @Override
    public ListenerCallBack OnReceiveGroupMsg(ServerMessage serverMessage) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param serverMessage
     * @return
     */
    @Override
    public ListenerCallBack OnReceiveTestMsg(ServerMessage serverMessage) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ListenerCallBack OnReceiveOnLineList(ServerMessage serverMessage) {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ListenerCallBack OnForcedOffLine() {
        return this;
    }
}
