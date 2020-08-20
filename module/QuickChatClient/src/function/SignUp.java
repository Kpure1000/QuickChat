package function;

import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;

import java.math.BigInteger;

public class SignUp extends BasicFunction {
    @Override
    public void Close() {
        //删除自己的监听
        ClientNetwork.getInstance().removeListenerCallBack(listenerCallBack);
        // TODO应该返回上一个登陆界面
    }

    /**
     * 输入注册信息
     *
     * @param newName    新用户名
     * @param password   创建密码
     * @param rePassword 确认密码
     */
    public void inputInformation(String newName, String password, String rePassword) {
        int judgeResult = judgePass(password, rePassword);
        if (judgeResult == 0) {
            sendSignUnMessage(newName, password);
        } else if (judgeResult == 1) {
            // 输入不合法
            signUpCallBack.OnPassInputError("输入不合法");
        } else if (judgeResult == 2) {
            // 密码输入不一致
            signUpCallBack.OnPassInputError("输入不一致");
        } else if (judgeResult == 3) {
            // 密码长度不合法
            signUpCallBack.OnPassInputError("长度不合法");
        }

    }

    /**
     * 发送注册申请给服务器
     *
     * @param newName
     * @param password
     */
    private void sendSignUnMessage(String newName, String password) {
        if (newName != null && password != null) {
            Debug.Log("ID:" + newName + ", PASS: " + password);

            // 定义监听来获取登录反馈
            listenerCallBack = new ListenerCallBackAdapter() {

                @Override
                public ListenerCallBack OnSignUpCallBack(BigInteger fbID) {
                    Debug.Log("监听回调获取到了-注册新ID的反馈: " + fbID);
                    if (fbID != null) {
                        signUpCallBack.OnNewIDFeedBack(fbID);

                    }
                    return this;
                }
            };
            // 加入监听
            ClientNetwork.getInstance().addListenerCallBack(listenerCallBack);
            // 发送验证请求
            ClientNetwork.getInstance().sendMessage(new UserMessage(
                    UserMessage.MessageType.Require_SignUp,
                    null, null,
                    newName.toString() + "#" + password
            ));
        }
        Debug.LogWarning("密码输入有误");
    }

    /**
     * 判断密码格式
     *
     * @param password
     * @return
     */
    private int judgePass(String password, String rePassword) {
        //输入有空
        if (password == null || rePassword == null)
            return 1;
        //判断两次输入是否一致
        if (!String.valueOf(password).equals(String.valueOf(rePassword))) {
            return 2;
        }
        //长度不正确
        if (password.length() < 6 || password.length() > 20) {
            return 3;
        }
        return 0;
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
        void OnPassInputError(String errorMsg);

    }

    /**
     * 本注册功能的回调实例
     */
    private SignUpCallBack signUpCallBack;

    private ListenerCallBack listenerCallBack;

}
