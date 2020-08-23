package function;

import java.math.BigInteger;

public interface ServerCallBack {
    public void OnUserOnLine(BigInteger useID);

    public void OnUserOffLine(BigInteger userID);
}
