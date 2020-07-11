package function;

import data.DataManager;
import network.ClientNetwork;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;
import java.util.ArrayList;

public class SignIn {
    public SignIn() {
        idListTmp = DataManager.getInstance().getIDRecord();
    }

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
            ClientNetwork.getInstance().addListener(new ListenerCallBackAdapter() {
                @Override
                public void OnSignInCallBack(boolean fbState) {
                    Debug.Log("获取到了值: "+fbState);
                }
            });
        }
    }

    // TODO 要把这里的部分回调去掉，function应该只负责处理请求，不负责反馈
    public interface SignInCallBack {
        /**
         * on function getting id record from local
         */
        void OnGetIDConfig(ArrayList<BigInteger> ids);

        /**
         * on function getting password config form local
         */
        void OnGetPassConfig(String password);

        /**
         * on function receive feed back, including ID and password
         */
        void OnReceiveFeedBack(boolean feedback);
    }

    SignInCallBack callBack;

    String idInputTmp;// the temp of id, from input

    ArrayList<BigInteger> idListTmp;// the temp of IDList, from public config

    String passTmp;// the temp of password, from input or private config

    boolean rememberPass;// pass record config from private config
}
