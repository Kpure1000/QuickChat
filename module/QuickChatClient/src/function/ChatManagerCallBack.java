package function;

import message.ServerMessage;
import message.UserMessage;

import java.math.BigInteger;
import java.util.ArrayList;

public interface ChatManagerCallBack {
    /**
     * When Force Close By Server
     */
    public void OnForceClose();

    public void OnSendMessageSuccess(UserMessage userMessage);

    public void OnReceivePrivateMsg(ServerMessage serverMessage);

    public void OnReceiveGroupMsg(ServerMessage serverMessage);

    public void OnReceiveTestMsg(ServerMessage serverMessage);

    public void OnMakeNotice(ServerMessage serverMessage);

    public void OnReceiveOnLineList(ArrayList<BigInteger>idList);
}
