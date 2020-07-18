package function;

import data.DataManager;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;
import java.util.ArrayList;

public class SignIn extends BasicFunction{
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
     * @param callBack 登录时直接反馈的回调（V-C)
     */
    public void setCallBack(SignInCallBack callBack) {
        this.callBack = callBack;
        callBack.OnGetIDConfig(idListTmp);
    }

    /**
     * 传入ID与密码
     * <p>
     * 在此进行回调UI
     * </p>
     *
     * @param ID
     * @param password
     */
    public void inputInformation(BigInteger ID, String password) {
        if (ID != null && password != null) {
            Debug.Log("ID:" + ID + ", PASS: " + password);
            //添加监听来获取登录反馈
            listenerCallBack = new ListenerCallBackAdapter() {
                @Override
                public ListenerCallBack OnSignInCallBack(boolean fbState) {
                    Debug.Log("获取到了值: " + fbState);
                    return this;
                }
            };
            ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);
        }
    }

    /**
     * 登录功能回调，功能与UI交互
     */
    public interface SignInCallBack {
        /**
         * 获取ID列表
         * @param ids
         */
        void OnGetIDConfig(ArrayList<BigInteger> ids);

        /**
         * 获取密码配置
         * @param password
         */
        void OnGetPassConfig(String password);
    }

    //本登录功能回调
    SignInCallBack callBack;

    //监听回调
    ListenerCallBackAdapter listenerCallBack;

    String idInputTmp;// the temp of id, from input

    ArrayList<BigInteger> idListTmp;// the temp of IDList, from public config

    String passTmp;// the temp of password, from input or private config

    boolean rememberPass;// pass record config from private config
}
