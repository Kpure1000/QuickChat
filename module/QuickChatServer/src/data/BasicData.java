package data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 数据基类
 */
public class BasicData implements Serializable {
    protected BigInteger ID;
    protected String Name;
    protected String createTime;

}
