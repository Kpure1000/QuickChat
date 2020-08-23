package information;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 群组信息
 */
public class GroupInfo extends BasicInfo{

    public GroupInfo(BigInteger ID, String Name) {
        super(ID, Name);
    }

    public BigInteger getGroupLeaderID() {
        return groupLeaderID;
    }

    @Deprecated
    public ArrayList<BigInteger> getMemberIDList() {
        return memberIDList;
    }

    /**
     * 添加成员
     * @param newID 新成员ID
     */
    public void addMember(BigInteger newID){
        for (BigInteger item :
                memberIDList) {
            if(item.compareTo(newID)==0){
                return;
            }
        }
        memberIDList.add(newID);
    }

    /**
     * 删除成员
     * @param targetID 目标成员ID
     */
    public void deleteMember(BigInteger targetID){
        memberIDList.removeIf(item -> item.compareTo(targetID) == 0);
    }

    public void setGroupLeaderID(BigInteger groupLeaderID) {
        this.groupLeaderID = groupLeaderID;
    }

    @Deprecated
    public void setMemberIDList(ArrayList<BigInteger> memberIDList) {
        this.memberIDList = memberIDList;
    }

    @Override
    public String toString() {
        // TODO 群组信息暂时这样toString
        return super.toString() + "#" + groupLeaderID;
    }

    /**群主ID*/
    private BigInteger groupLeaderID;

    /**群成员信息*/
    private ArrayList<BigInteger> memberIDList;

}
