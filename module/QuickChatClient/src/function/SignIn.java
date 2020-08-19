package function;

import data.DataManager;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;
import java.util.ArrayList;

public class SignIn extends BasicFunction {
    public SignIn() {
        idListTmp = DataManager.getInstance().getIDRecord();
    }

    @Override
    public void Close() {
        //删除监听回调
        ClientNetwork.getInstance().removeListenerCallBacl(listenerCallBack);
        ClientNetwork.getInstance().Disconnect();
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
     * @return
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
            listenerCallBack = new ListenerCallBackAdapter() {
                @Override
                public ListenerCallBack OnListeningStart() {
                    Debug.Log("关于登陆的监听开始工作");
                    return this;
                }

                @Override
                public ListenerCallBack OnSignInCallBack(String fbState) {
                    Debug.Log("监听回调获取到了-登陆请求的反馈: " + fbState);
                    if (String.valueOf(fbState).equals("pass")) {
                        //更新ID记录
                        if (signInCallBack.OnNeedPassConfigUpdate()) {
                            DataManager.getInstance().updateIDRecord(ID, password);
                        } else {
                            DataManager.getInstance().updateIDRecord(ID, null);
                        }
                        //登录成功
                        signInCallBack.OnSignInSuccess();
                    }else if (String.valueOf(fbState).equals("failed")) {
                        //登录失败
                        signInCallBack.OnSignInFailed();
                    }
                    return this;
                }
            };
            // 加入监听
            ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);
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
     * 登录功能回调，功能与UI交互
     */
    public interface SignInCallBack {
        /**
         * 获取ID列表
         *
         * @param ids 登录成功的ID列表
         */
        void OnGetIDConfig(ArrayList<BigInteger> ids);

        /**
         * 获取密码配置
         *
         * @param password 记住的密码，如果不记住则为null
         */
        void OnGetPassConfig(String password);

        void OnIDInputError();

        void OnPassInputError();

        /**
         * 如果用户始终没有变化勾选框，则主动向UI请求记住密码配置
         *
         * @return UI记住密码选框的值
         */
        boolean OnNeedPassConfigUpdate();

        void OnSignInSuccess();

        void OnSignInFailed();
    }

    /**
     * 本登录功能的回调
     */
    private SignInCallBack signInCallBack;

    /**
     * 监听回调
     */
    private ListenerCallBackAdapter listenerCallBack;

    @Deprecated
    private String idInputTmp;// the temp of id, from input

    /**
     * 登录成功的历史ID的列表
     */
    private ArrayList<BigInteger> idListTmp;// the temp of IDList, from public config

    /**
     * 可能用来缓存是否记住密码以及其值，如果不记住则为null
     */
    //private String passTmp;// the temp of password, from input or private config

    @Deprecated
    private boolean rememberPass;// pass record config from private config
}
