package information;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 群组信息
 */
public class GroupInfo extends BasicInfo{

    /**群主ID*/
    private BigInteger groupLeaderID;

    /**群成员信息*/
    private ArrayList<BigInteger> memberIDList;

    public BigInteger getGroupLeaderID() {
        return groupLeaderID;
    }

    public void setGroupLeaderID(BigInteger groupLeaderID) {
        this.groupLeaderID = groupLeaderID;
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

    @Deprecated
    public ArrayList<BigInteger> getMemberIDList() {
        return memberIDList;
    }

    @Deprecated
    public void setMemberIDList(ArrayList<BigInteger> memberIDList) {
        this.memberIDList = memberIDList;
    }
}
