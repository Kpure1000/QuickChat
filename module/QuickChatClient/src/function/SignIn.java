package function;

import data.DataManager;
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
            //添加监听来获取登录反馈
            listenerCallBack = new ListenerCallBackAdapter() {
                @Override
                public ListenerCallBack OnSignInCallBack(int fbState) {
                    Debug.Log("监听回调获取到了-登陆请求的反馈: " + fbState);
                    return this;
                }
            };
            ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);
        }
    }

    public boolean getPasswordConfig(String targetID) {
        // TODO 从私人配置加载密码配置
        //DataManager.getInstance()
        return false;
    }

    public void setPasswordConfig(String targetID, boolean config) {
        if (getPasswordConfig(targetID)) {
            // TODO 修改私人配置
        }
    }

    /**
     * 登录功能回调，功能与UI交互
     */
    public interface SignInCallBack {
        /**
         * 获取ID列表
         *
         * @param ids
         */
        void OnGetIDConfig(ArrayList<BigInteger> ids);

        /**
         * 获取密码配置
         *
         * @param password
         */
        void OnGetPassConfig(String password);

        void OnIDInputError();

        void OnPassInputError();
    }

    //本登录功能回调
    SignInCallBack signInCallBack;

    //监听回调
    ListenerCallBackAdapter listenerCallBack;

    String idInputTmp;// the temp of id, from input

    ArrayList<BigInteger> idListTmp;// the temp of IDList, from public config

    String passTmp;// the temp of password, from input or private config

    boolean rememberPass;// pass record config from private config
}
