package network;

import function.Debug;
import message.ServerMessage;

public interface ServerListenerCallBack {
    void OnSendMessageSuccess(ServerMessage message);
    void OnSendMessageFailed(ServerMessage message);
}
