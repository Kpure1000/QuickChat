package network;

import message.ServerMessage;

import java.math.BigInteger;

public interface ListenerCallBack {
    public void OnSignInCallBack(boolean fbState);

    public void OnSignUpCallBack(BigInteger fbID);

    public void OnReceivePrivateMsg(); //  TODO 缺少参数

    public void OnReceiveGroupMsg();

    public void OnReceiveTestMsg();
}
