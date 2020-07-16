package network;

import function.Debug;
import message.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
     * @param receiver 接受者
     * @param listenerCallBack 回调
     */
    public synchronized void addListenerCallBack(Object receiver, ListenerCallBack listenerCallBack) {
        if (listenerCallBack != null) {
            callBackMap.put(receiver, listenerCallBack);
        }
    }

    /**
     * 删除监听回调
     *
     * @param receiver 接受者
     */
    public synchronized void removeListenerCallBack(Object receiver) {
        if (receiver != null) {
            callBackMap.remove(receiver);
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
                            for (Object Key :
                                    callBackMap.keySet()) {
                                if (callBackMap.get(Key) != null) {
                                    callBackMap.get(Key).OnSignInCallBack(
                                            Boolean.parseBoolean(sM.getMessage()) //  获取是否登录成功
                                    );
                                }
                            }
                        }
                        case Fb_SignUp -> { //  注册反馈
                            for (Object Key :
                                    callBackMap.keySet()) {
                                if (callBackMap.get(Key) != null) {
                                    callBackMap.get(Key).OnSignUpCallBack(
                                            sM.getSenderID() //  获取注册者的ID（新生成的）
                                    );
                                }
                            }
                        }
                        case Fb_OnlineList -> {
                            for (Object Key :
                                    callBackMap.keySet()) {
                                if (callBackMap.get(Key) != null) {
                                    callBackMap.get(Key).OnReceiveOnLineList(
                                            // TODO 应该是缺少一个ArrayList
                                    );
                                }
                            }
                        }
                        case Msg_Private -> {
                            for (Object Key :
                                    callBackMap.keySet()) {
                                if (callBackMap.get(Key) != null) {
                                    callBackMap.get(Key).OnReceivePrivateMsg(
                                            // TODO 缺少生产的消息
                                    );
                                }
                            }
                        }
                        case Msg_Group -> {
                            for (Object Key :
                                    callBackMap.keySet()) {
                                if (callBackMap.get(Key) != null) {
                                    callBackMap.get(Key).OnReceiveGroupMsg(
                                            // TODO 缺少参数
                                    );
                                }
                            }
                        }
                        case Msg_Test -> {
                            for (Object Key :
                                    callBackMap.keySet()) {
                                if (callBackMap.get(Key) != null) {
                                    callBackMap.get(Key).OnReceiveTestMsg(
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

    // 回调的Hash表
    private HashMap<Object, ListenerCallBack> callBackMap;

    private Socket socket;
}
