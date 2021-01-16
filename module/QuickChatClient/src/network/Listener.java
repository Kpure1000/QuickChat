package network;

import data.UserManager;
import function.Debug;
import message.ServerMessage;
import message.UserMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监听线程，接收服务器消息
 */
public class Listener implements Runnable {

    /**
     * 监听开启标志
     */
    private boolean listening;

    /**
     * 构造一个监听线程
     *
     * @param socket 唯一套接字
     */
    public Listener(Socket socket) {
        this.socket = socket;
        listening = true;
        Debug.Log("Listener线程开始监听!");
    }

    /**
     * 添加监听回调
     *
     * @param listenerCallBack 新建回调
     */
    public void addListenerCallBack(ListenerCallBack listenerCallBack) {
        Debug.Log("监听准备加入列表");
            if (listenerCallBack != null) {
                for (ListenerCallBack item :
                        listenerCallBackList) {
                    if (item == listenerCallBack) return;
                }
                for (var item :
                        listenerCallBackList) {
                    if (item.equals(listenerCallBack))
                        //若已存在则不加入
                        return;
                }
                listenerCallBackList.add(listenerCallBack);
                if (listening) {
                    listenerCallBack.OnListeningStart();
                }
            }

        Debug.Log("监听加入列表完毕");
    }

    /**
     * 删除监听回调
     *
     * @param listenerCallBack 目标回调
     */
    public void removeListenerCallBack(ListenerCallBack listenerCallBack) {
            if (listenerCallBack != null) {
                listenerCallBackList.remove(listenerCallBack);
            }
    }

    /**
     * 关闭监听
     */
    public void Close() {
        // TODO 关闭监听
        listening = false;
            listenerCallBackList.clear();
        Debug.Log("准备关闭监听");
    }

    @Override
    public void run() {
        try {
            try {
                objIn = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                Debug.LogWarning("获取到的socket输入输出有问题,不能开始监听");
                e.printStackTrace();
                listening = false;
            }
            while (listening) {
                ServerMessage serverMessage = (ServerMessage) objIn.readObject(); //  获取消息对象
                if (serverMessage.getContent() == null) {
                    break;
                }
//                synchronized (listenerCallBackList) {
                    switch (serverMessage.getMessageType()) {
                        case Fb_SignIn -> { //  登录反馈
                            // 在这里调用有关回调即可
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnSignInCallBack(serverMessage.getContent()); //  获取是否登录成功
                                }
                            }
                        }
                        case Fb_SignUp -> { //  注册反馈
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnSignUpCallBack(
                                            serverMessage.getReceiverID() //  获取注册者的ID（新生成的）
                                    );
                                }
                            }
                        }
                        case Fb_FriendRequire -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnFriendRequireCallBack(serverMessage);
                                }
                            }
                        }
                        case Fb_OnlineList -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceiveOnLineList(serverMessage);
                                }
                            }
                        }
                        case Require_ForcedOffLine -> {
                            // TODO强制下线
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnForcedOffLine();
                                }
                            }
                            Debug.Log("由于检测到账户不安全，已被强制下线");
                            ClientNetwork.getInstance().sendMessage(new UserMessage(UserMessage.MessageType.Require_Offline,
                                    UserManager.getInstance().getUserInfo().getID(),null,""));
                            Close();
                        }
                        case Msg_Private -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceivePrivateMsg(serverMessage);
                                }
                            }
                        }
                        case Msg_Group -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceiveGroupMsg(serverMessage);
                                }
                            }
                        }
                        case Msg_Test -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceiveTestMsg(serverMessage);
                                }
                            }
                        }
                    } //  end of switch
//                } //  end of synchronized
            } //  end of while(listening)
        } catch (SocketException e) {
            Debug.LogWarning("Socket已关闭，应该是某窗口关闭导致的，Listener线程马上退出");
            //e.printStackTrace();
        } catch (IOException e) {
            Debug.LogError("监听IO异常");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // 消息错误
            Debug.LogError("The Msg is Empty or UnSerialize Failed.");
            e.printStackTrace();
        }
    }

    // 回调列表
    final private CopyOnWriteArrayList<ListenerCallBack> listenerCallBackList = new CopyOnWriteArrayList<>();

    private Socket socket;

    ObjectInputStream objIn;
}
