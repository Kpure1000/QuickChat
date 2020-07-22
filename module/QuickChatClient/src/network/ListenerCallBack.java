package network;

import java.math.BigInteger;

public interface ListenerCallBack {
    public ListenerCallBack OnSignInCallBack(int fbState); //  TODO 登录反馈值的数据类型，暂定int(或者专门弄一个枚举)

    public ListenerCallBack OnSignUpCallBack(BigInteger fbID);

    public ListenerCallBack OnReceivePrivateMsg(); //  TODO 缺少参数

    public ListenerCallBack OnReceiveGroupMsg();

    public ListenerCallBack OnReceiveTestMsg();

    public ListenerCallBack OnReceiveOnLineList(); //  TODO 缺参数
}
