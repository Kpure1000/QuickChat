package function;

import data.DataManager;
import data.UserManager;
import information.UserInfo;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;
import network.NetCallBack;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 登录功能.
 * <p>拓展注册功能</p>
 *
 * @see function.SignUp
 */
public class SignIn extends BasicFunction {
    public SignIn() {
        idListTmp = DataManager.getInstance().getIDRecord();

        netCallBack_function = new NetCallBack() {
            @Override
            public void OnConnectSuccess() {
                //DO NOTHING
            }

            @Override
            public void OnConnectFailed() {
                // DO NOTHING
            }

            @Override
            public void OnDisconnect() {
                // DO NOTHING
            }

            @Override
            public void OnSendMessageSuccess(UserMessage msg) {
                Debug.Log("发送" + msg.getMessageType() + "类消息成功");
            }

            @Override
            public void OnSendMessageFailed(UserMessage msg) {
                Debug.LogWarning("发送" + msg.getMessageType() + "类消息失败");
            }
        };
        ClientNetwork.getInstance().addNetCallBack(netCallBack_function);
    }

    @Override
    public void Close() {
        //删除监听回调
        ClientNetwork.getInstance().removeListenerCallBack(listenerCallBack);
    }

    /**
     * 设置登录功能回调
     *
     * @param signInCallBack 登录时直接反馈的回调（V-C)
     */
    public void setSignInCallBack(SignInCallBack signInCallBack) {
        this.signInCallBack = signInCallBack;
        signInCallBack.OnGetIDConfig(idListTmp);
    }

    /**
     * 输入ID与密码
     *
     * @param ID
     * @param password
     */
    public void inputInformation(String ID, String password) {
        if (judgeID(ID)) {
            if (judgePass(password)) {
                sendSignInMessage(new BigInteger(ID), password);
            } else {
                //密码格式错误
                signInCallBack.OnPassInputError();
            }
        } else {
            //ID格式错误
            signInCallBack.OnIDInputError();
        }
    }

    /**
     * 判断ID格式<p>
     * id为数字，4~15位
     *
     * @param id 输入的ID
     * @return
     */
    private boolean judgeID(String id) {
        //判断长度
        if (id != null && id.length() >= 4 && id.length() <= 15) {
            StringBuilder idCharList = new StringBuilder(id);
            for (int i = 0; i < idCharList.length(); i++) {
                //寻找非数字
                if (idCharList.charAt(i) < '0' || idCharList.charAt(i) > '9') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断密码格式
     *
     * @param password 输入的密码
     * @return 是否输入正确
     */
    private boolean judgePass(String password) {
        //判断长度
        if (password != null && password.length() >= 6 && password.length() <= 16) {
            StringBuilder passCharList = new StringBuilder(password);
            for (int i = 0; i < passCharList.length(); i++) {
                //寻找空格
                if (passCharList.charAt(i) == ' ') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 发送ID与密码
     *
     * @param ID
     * @param password
     */
    private void sendSignInMessage(BigInteger ID, String password) {
        if (ID != null && password != null) {
            Debug.Log("ID:" + ID + ", PASS: " + password);

            // 定义监听来获取登录反馈
            if (listenerCallBack == null) {
                //若listenerCallback为空则实例化一个
                listenerCallBack = new ListenerCallBackAdapter() {
                    @Override
                    public ListenerCallBack OnListeningStart() {
                        Debug.Log("关于登陆的监听开始工作");
                        return this;
                    }

                    @Override
                    public ListenerCallBack OnSignInCallBack(String fbState) {
                        Debug.Log("监听回调获取到了-登陆请求的反馈: " + fbState);
                        String[] fbStr = fbState.split("#");
                        if (String.valueOf(fbStr[0]).equals("pass")) {
                            //更新ID记录
                            DataManager.getInstance().updateIDRecord(ID,
                                    signInCallBack.OnNeedPassConfigUpdate() ? password : null);
                            //登录成功
                            signInCallBack.OnSignInSuccess();
                            //本地登录
                            // TODO 服务器反馈登录消息时，要增加一个UserInfo的详细反馈，并且传入这个setUserInfo
                            UserManager.getInstance().setUserInfo(new UserInfo(new BigInteger(fbStr[1]), fbStr[2]));
                        } else if (fbState.equals("failed")) {
                            //登录失败
                            signInCallBack.OnSignInFailed();
                        }
                        return this;
                    }
                };
                // 加入监听
                ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);
            }

            // 发送验证请求
            ClientNetwork.getInstance().sendMessage(new UserMessage(
                    UserMessage.MessageType.Check_SignIn_ID,
                    ID,
                    null,
                    ID.toString() + "#" + password
            ));
        }
    }

    /**
     * 从私人配置加载密码配置，仅当ID下拉菜单变化时调用
     *
     * @param targetID 目标ID
     */
    public void getPasswordConfig(String targetID) {
        // TODO从私人配置加载密码配置
        // 通过回调反馈记住密码配置
        signInCallBack.OnGetPassConfig(
                DataManager.getInstance().getPasswordConfig(new BigInteger(targetID)));
    }

    /**
     * 按ID更新记住密码配置，仅当勾选框变化时调用
     *
     * @param targetID 目标ID
     * @param config   是否记住密码
     */
    public void setPasswordConfig(String targetID, boolean config) {
        var ID = new BigInteger(targetID);
        if (DataManager.getInstance().getPasswordConfig(ID) != null) {
            // TODO 修改私人配置
            DataManager.getInstance().updatePasswordConfig(ID, config);
        }
    }

    /**
     * 本登录功能的回调
     */
    private SignInCallBack signInCallBack;

    /**
     * 监听回调
     */
    private ListenerCallBackAdapter listenerCallBack;

    /**
     * 登录成功的历史ID的列表
     */
    private ArrayList<BigInteger> idListTmp;// the temp of IDList, from public config

}
