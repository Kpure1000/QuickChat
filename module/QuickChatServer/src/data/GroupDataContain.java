package data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 群组数据容器
 */
public class GroupDataContain implements Serializable {

    static final long serialVersionUID = 0x784fdd5b59ea1267L;

    public GroupDataContain() {
        maxID = new BigInteger("100000");
    }

    /**
     * 获取群组信息数据
     * @param ID 目标群组ID
     * @return 群组数据
     */
    public GroupData getGroupData(BigInteger ID) {
        return groupDataMap.getOrDefault(ID, null);
    }

    /**
     * 获取群成员ID列表
     * @param ID 目标群ID
     * @return 群成员列表，不存在群则为空
     */
    public ArrayList<BigInteger> getMemberList(BigInteger ID){
        GroupData groupData = getGroupData(ID);
        if(groupData!=null){
            ArrayList<BigInteger> reList = new ArrayList<>(groupData.getMemberID());
            return reList;
        }
        return null;
    }

    /**
     * 设置群名
     * @param ID
     * @param newName
     */
    public void setGroupName(BigInteger ID,String newName){
        GroupData groupData = getGroupData(ID);
        if(groupData!=null){
            groupData.setName(newName);
        }
    }

    /**
     * 新建群组
     *
     * @param Name
     * @param memberIDList
     * @return
     */
    public BigInteger CreateNewGroup(String Name, ArrayList<BigInteger> memberIDList) {
        BigInteger newID = maxID.add(new BigInteger("1"));
        maxID = newID;
        groupDataMap.put(newID, new GroupData(newID, Name, memberIDList));
        return newID;
    }

    /**
     * 群组散列表
     */
    private final ConcurrentHashMap<BigInteger, GroupData> groupDataMap = new ConcurrentHashMap<>();

    /**
     * 当前最大群组ID，用于生成新群组
     */
    private BigInteger maxID;
}
