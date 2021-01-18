package data;

import message.ServerMessage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 群组数据
 * <p>
 * 注：memberID包含adminID，grouper属于adminID
 * // TODO 可能要改一下方式，加一个类来表示群员不同的身份
 * </p>
 */
public class GroupData extends BasicData {

    public GroupData(BigInteger newID, String name) {
        super(newID, name);
        messageRecord = new MessageRecord(newID);
    }

    public GroupData(BigInteger newID, String name, CopyOnWriteArrayList<BigInteger> memberID) {
        super(newID, name);
        messageRecord = new MessageRecord(newID);
        this.memberID = memberID;
    }

    public GroupData(BigInteger newID, String name, ArrayList<BigInteger> memberIDList) {
        super(newID, name);
        messageRecord = new MessageRecord(newID);
        memberID.addAll(memberIDList);
    }

    /**
     * 加入成员
     *
     * @param ID 新成员
     */
    public void addMember(BigInteger ID) {
        for (BigInteger item :
                memberID) {
            if (item.compareTo(ID) == 0) {
                return;
            }
        }
        memberID.add(ID);
    }

    /**
     * 删除成员
     *
     * @param ID 目标成员
     */
    public void removeMember(BigInteger ID) {
        memberID.remove(ID);
    }

    /**
     * 是否在群内
     *
     * @param ID 目标ID
     * @return 是否在群内
     */
    public boolean isInGroup(BigInteger ID) {
        return memberID.contains(ID);
    }

    /**
     * 获取群成员列表
     *
     * @return 成员列表（线程安全）
     */
    public CopyOnWriteArrayList<BigInteger> getMemberID() {
        return memberID;
    }

    /**
     * 获取成员个数
     *
     * @return 群成员个数
     */
    public int getMemberNumber() {
        return memberID.size();
    }

    /**
     * 添加消息记录
     * @param serverMessage 反馈消息
     */
    public void addMessageRecord(ServerMessage serverMessage) {
        messageRecord.addMessageRecord(new MessageContent(serverMessage));
    }

    /**
     * 组员ID
     */
    private CopyOnWriteArrayList<BigInteger> memberID = new CopyOnWriteArrayList<>();

    /**
     * 消息记录
     */
    private final MessageRecord messageRecord;
}
