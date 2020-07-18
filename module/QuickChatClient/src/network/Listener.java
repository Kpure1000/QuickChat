package network;

import function.Debug;
import message.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 监听线程，接收服务器消息
 */
public class Listener implements Runnable {

    /**
     * 构造一个监听线程
     *
     * @param socket 唯一套接字
     */
    public Listener(Socket socket) {
        this.socket = socket;
    }

    /**
     * 添加监听回调
     *
     * @param listenerCallBack 新建回调
     */
    public synchronized void addListenerCallBack(ListenerCallBack listenerCallBack) {
        if (listenerCallBack != null) {
            listenerCallBackList.add(listenerCallBack);
        }
    }

    /**
     * 删除监听回调
     *
     * @param listenerCallBack 目标回调
     */
    public synchronized void removeListenerCallBack(ListenerCallBack listenerCallBack) {
        if (listenerCallBack != null) {
            listenerCallBackList.remove(listenerCallBack);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
                ServerMessage sM = (ServerMessage) objIn.readObject(); //  获取消息对象
                if (sM.getMessage() != null) {
                    switch (sM.getMessageType()) {
                        case Fb_SignIn -> { //  登录反馈
                            // 在这里调用有关回调即可
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnSignInCallBack(
                                            Boolean.parseBoolean(sM.getMessage()) //  获取是否登录成功
                                    );
                                }
                            }
                        }
                        case Fb_SignUp -> { //  注册反馈
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnSignUpCallBack(
                                            sM.getSenderID() //  获取注册者的ID（新生成的）
                                    );
                                }
                            }
                        }
                        case Fb_OnlineList -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceiveOnLineList(
                                            // TODO 应该是缺少一个ArrayList
                                    );
                                }
                            }
                        }
                        case Msg_Private -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item!= null) {
                                    item.OnReceivePrivateMsg(
                                            // TODO 缺少生产的消息
                                    );
                                }
                            }
                        }
                        case Msg_Group -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceiveGroupMsg(
                                            // TODO 缺少参数
                                    );
                                }
                            }
                        }
                        case Msg_Test -> {
                            for (var item :
                                    listenerCallBackList) {
                                if (item != null) {
                                    item.OnReceiveTestMsg(
                                            // TODO 缺少参数
                                    );
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // 消息错误
            Debug.LogError("The Msg is Empty or UnSerialize Failed.");
            e.printStackTrace();
        }
    }

    // 回调列表
    private ArrayList<ListenerCallBack> listenerCallBackList;

    private Socket socket;
}
