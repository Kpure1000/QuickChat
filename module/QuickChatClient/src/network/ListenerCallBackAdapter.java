package network;

import java.math.BigInteger;

/**
 * 监听回调事件的抽象适配器
 *
 * @author Kpurek
 * @version 1.0
 * @see network.ListenerCallBack
 */
public abstract class ListenerCallBackAdapter implements ListenerCallBack {

    /**
     * {@inheritDoc}
     *
     * @param fbState
     */
    public ListenerCallBack OnSignInCallBack(int fbState) {
        return this;
    }

    public ListenerCallBack OnSignUpCallBack(BigInteger fbID) {
        return this;
    }

    //  TODO 缺少参数
    public ListenerCallBack OnReceivePrivateMsg() {
        return this;
    }

    public ListenerCallBack OnReceiveGroupMsg() {
        return this;
    }

    public ListenerCallBack OnReceiveTestMsg() {
        return this;
    }

    public ListenerCallBack OnReceiveOnLineList(){
        return this;
    }
}
