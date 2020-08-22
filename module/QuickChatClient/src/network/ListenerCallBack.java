package network;

import message.ServerMessage;

import java.math.BigInteger;

/**
 * 监听任务的回调接口.
 * <p>
 * 所有方法的返回值为ListenerCallBack是为了方便复合回调，
 * 链式回调看起来会比较简介;
 * </p>
 * <p>
 * 一般使用该接口的适配器进行实际工作
 *
 * @see ListenerCallBackAdapter
 * </p>
 */
public interface ListenerCallBack {

    /**
     * 开始监听，只在addListenerCallBack时使用
     *
     * @return ListenerCallBack
     */
    public ListenerCallBack OnListeningStart();

    /**
     * 登陆反馈
     *
     * @param fbState 反馈的登录结果
     * @return ListenerCallBack
     */
    public ListenerCallBack OnSignInCallBack(String fbState);

    /**
     * 注册反馈
     *
     * @param fbID 反馈的新注册ID
     * @return ListenerCallBack
     */
    public ListenerCallBack OnSignUpCallBack(BigInteger fbID);

    /**
     * 好友申请反馈
     *
     * @param fbState 反馈申请结果
     * @return
     */
    public ListenerCallBack OnFriendRequireCallBack(ServerMessage serverMessage);

    /**
     * 私聊反馈
     *
     * @param serverMessage 服务器反馈的消息
     * @return ListenerCallBack
     */
    public ListenerCallBack OnReceivePrivateMsg(ServerMessage serverMessage);

    /**
     * 群聊反馈
     *
     * @param serverMessage 服务器反馈的消息
     * @return ListenerCallBack
     */
    public ListenerCallBack OnReceiveGroupMsg(ServerMessage serverMessage);

    /**
     * 测试消息反馈
     *
     * @param serverMessage 服务器反馈的消息
     * @return ListenerCallBack
     */
    public ListenerCallBack OnReceiveTestMsg(ServerMessage serverMessage);

    /**
     * 在线列表反馈
     *
     * @return ListenerCallBack
     */
    public ListenerCallBack OnReceiveOnLineList(); //  TODO 缺参数

    /**
     * 强制下线
     *
     * @return ListenerCallBack
     */
    public ListenerCallBack OnForcedOffLine();
}
