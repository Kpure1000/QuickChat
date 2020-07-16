package network;

import java.math.BigInteger;

public interface ListenerCallBack {
    public ListenerCallBack OnSignInCallBack(boolean fbState);

    public ListenerCallBack OnSignUpCallBack(BigInteger fbID);

    public ListenerCallBack OnReceivePrivateMsg(); //  TODO 缺少参数

    public ListenerCallBack OnReceiveGroupMsg();

    public ListenerCallBack OnReceiveTestMsg();

    public ListenerCallBack OnReceiveOnLineList(); //  TODO 缺参数
}
