package information;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Basic of information
 * <p>
 * 每个对象有一个唯一ID，
 * 包括用户和群组，
 * 名称不唯一，可修改
 * </p>
 */
public class BasicInfo implements Serializable {

    private static final long serialVersionUID = 0x6ecb4d6491b8df09L;

    public BasicInfo(BigInteger ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }

    public BigInteger getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getCreateTime() {
        return createTime;
    }

    /**
     * 用户不可修改ID，因此在此删除，不过在服务器端不会删除该方法
     *
     * @param ID
     */
    @Deprecated
    public void setID(BigInteger ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    private BigInteger ID;

    private String Name;

    private String createTime;

    @Override
    public String toString() {
        return ID + "#" + Name;
    }
}
