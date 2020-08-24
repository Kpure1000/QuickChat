package data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 数据基类
 */
public class BasicData implements Serializable {

    private static final long serialVersionUID = 0xd365142df7629b67L;

    protected BigInteger ID;
    protected String Name;
    protected String createTime;

    public BasicData(BigInteger ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }

    public void setID(BigInteger ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    @Override
    public String toString() {
        return ID + "#" + Name + "#" + createTime;
    }
}
