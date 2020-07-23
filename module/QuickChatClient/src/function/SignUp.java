package function;

import java.math.BigInteger;

public class SignUp extends BasicFunction {
    @Override
    public void Close() {
        // TODO 重写
        super.Close();
    }

    public void inputInformation(String newName, String password) {
        if (judgePass(password)) {
            sendSignInMessage(newName, password);
        } else {
            //密码格式错误
            signUpCallBack.OnPassInputError();
        }

    }

    /**
     * 发送注册申请给服务器
     * @param newName
     * @param password
     */
    private void sendSignInMessage(String newName, String password) {

    }

    /**
     * 判断密码格式
     * @param password
     * @return
     */
    private boolean judgePass(String password) {
        return false;
    }

    /**
     * 设置注册回调
     *
     * @param signUpCallBack 注册回调
     */
    public void setSignUpCallBack(SignUpCallBack signUpCallBack) {
        this.signUpCallBack = signUpCallBack;
    }

    /**
     * 本注册功能的回调接口定义
     */
    public interface SignUpCallBack {
        /**
         * 获取到服务器的新ID
         *
         * @param newID
         */
        void OnNewIDFeedBack(BigInteger newID);

        /**
         * 输入的密码不符合要求
         */
        void OnPassInputError();

    }

    /**
     * 本注册功能的回调实例
     */
    private SignUpCallBack signUpCallBack;

}
