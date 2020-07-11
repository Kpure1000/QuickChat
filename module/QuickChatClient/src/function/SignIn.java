package function;

import data.DataManager;

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
    public void inputInfomation(BigInteger ID, String password) {

    }

    // TODO 要把这里的回调去掉，function应该只负责处理请求，不负责反馈
    @Deprecated
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
