package network;

import function.Debug;
import message.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 监听线程，接收服务器消息
 */
public class Listener extends Thread {

    /**
     * 构造一个监听线程
     *
     * @param socket 唯一套接字
     */
    public Listener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
                ServerMessage sM = (ServerMessage) objIn.readObject(); //  获取消息对象
                switch (sM.getMessageType()) {
                    // TODO 补全行为，要考虑线程安全问题
                    case Fb_SignIn -> {
                        // TODO 当收到服务器关于登录的反馈时，只需返回给唯一的登录功能对象即可
                        // 在这里调用有关回调即可
                    }
                    case Fb_SignUp -> {
                        // TODO 同上
                    }
                    case Fb_OnlineList -> {
                    }
                    case Msg_Private -> {
                        // TODO 私聊消息，要通知聊天对象进行记录
                        // 可能需要调用聊天对象管理对象来获取，线程应该安全
                    }
                    case Msg_Group -> {
                        // TODO 同上
                    }
                    case Msg_Test -> {
                        // TODO 同上，只不过可能通过弹窗显示（阻塞主线程）
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

    private ListenerCallBack listenerCallBack;

    private Socket socket;
}
