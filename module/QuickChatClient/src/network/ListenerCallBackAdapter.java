package network;

import java.math.BigInteger;

/**
 * 监听回调事件的抽象适配器
 *
 * @author Kpurek
 * @version 1.0
 *
 * @see network.ListenerCallBack
 */
public abstract class ListenerCallBackAdapter implements ListenerCallBack {

    /**
     * {@inheritDoc}
     * @param fbState
     */
    public void OnSignInCallBack(boolean fbState) {
    }

    public void OnSignUpCallBack(BigInteger fbID) {
    }

    //  TODO 缺少参数
    public void OnReceivePrivateMsg() {
    }

    public void OnReceiveGroupMsg() {
    }

    public void OnReceiveTestMsg() {
    }
}
