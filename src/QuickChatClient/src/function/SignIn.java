package function;

import data.DataManager;

import java.math.BigInteger;
import java.util.ArrayList;

public class SignIn {
    public SignIn(){
        DataManager.getInstance();
    }

    public void setCallBack(SignInCallBack callBack) {
        this.callBack = callBack;
    }

    public void inputID(BigInteger id){

    }

    public void inputPassword(String pass){

    }

    public interface SignInCallBack{
        void OnGetIDConfig(ArrayList<BigInteger> ids);// on function getting id record from local
        void OnGetPassConfig(String password);// on function getting password config form local
        void OnReceiveFeedBack(boolean feedback);// on function receive feed back, including ID and password
    }

    SignInCallBack callBack;

    String idInputTmp;// the temp of id, from input

    String idListTmp;// the temp of IDList, from public config

    String passTmp;// the temp of password, from input or private config

    boolean rememberPass;// pass record config from private config
}
