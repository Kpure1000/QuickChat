package function;

import java.math.BigInteger;
import java.util.ArrayList;
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
