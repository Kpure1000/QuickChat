package data;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 群组数据
 * <p>
 *     注：memberID包含adminID，grouper属于adminID
 *     // TODO 可能要改一下方式，加一个类来表示群员不同的身份
 * </p>
 */
public class GroupData extends BasicData{
    /**
     * 组员ID
     */
    private ArrayList<BigInteger> memberID;

    /**
     * 管理员ID
     */
    private ArrayList<BigInteger> adminID;

    /**
     * 群主ID
     */
    private BigInteger grouper;
}
