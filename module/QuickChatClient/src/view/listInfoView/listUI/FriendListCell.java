package view.listInfoView.listUI;

import function.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigInteger;

/**
 * 好友列表单元
 *
 * @see ListCell
 */
public class FriendListCell extends ListCell {

    /**
     * 构造好友列表单元，这已经是最底层的实现了
     *
     * @param icon             图标
     * @param name             好友名称
     * @param message          最新消息
     * @param selectedCallBack 被点击选择的回调接口
     */
    public FriendListCell(ImageIcon icon, BigInteger id, String name, String message, SelectedCallBack selectedCallBack) {

        super();

        this.cellIcon = icon;
        this.id = id;
        this.cellName = name;
        this.cellMessage = message;
        this.selectedCallBack = selectedCallBack;

        //被选中
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FriendListCell.this.selectedCallBack.OnSelected(FriendListCell.this);
                //取消提醒状态
                hasNewMessage = false;
            }
        });

        cellIcon.setImage(cellIcon.getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
        this.setIcon(cellIcon);

        this.setText(getFormatText());

        this.setIconTextGap(10);

    }

    /**
     * 设置是否为提示状态
     */
    public void setNotice(boolean notice) {
        hasNewMessage = notice;
        setBackground(hasNewMessage ? noticeColor : normalColor);
    }

    public boolean isHasNewMessage() {
        return hasNewMessage;
    }

    public String getCellMessage() {
        return cellMessage;
    }

    public String getCellName() {
        return cellName;
    }

    public ImageIcon getCellIcon() {
        return cellIcon;
    }

    public BigInteger getID() {
        return id;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public void setCellMessage(String cellMessage) {
        this.cellMessage = cellMessage;
    }

    /**
     * 获取HTML化的所有文本
     *
     * @return
     */
    public String getFormatText() {
        return "<html><font size=\"5\" style = \"color:#000000\">" + cellName + "</font><br/>" +
                "<font size=\"3\" style = \"color:#000000\">" + cellMessage + "</font></html>";
    }

    /**
     * 获取HTML化的名称文本
     *
     * @return
     */
    public String getFormatName() {
        return "<html><font size=\"6\" style = \"color:#89FF57\">" + cellName + "</font></html>";
    }

    /**
     * 设置图标
     *
     * @param cellIcon
     */
    public void setCellIcon(ImageIcon cellIcon) {
        this.cellIcon = cellIcon;
    }

    private BigInteger id;

    private String cellName;

    private String cellMessage;

    private ImageIcon cellIcon;

    public interface SelectedCallBack {
        public void OnSelected(FriendListCell friendListCell);
    }

    /**
     * 重写设置高光，添加提醒状态
     * <p>
     * 当处于提醒状态时，不能被置为normal状态
     * </p>
     *
     * @param selected 是否被选中
     */
    @Override
    public void setHighlight(boolean selected) {
        setBackground(selected ? highlightColor : (hasNewMessage ? noticeColor : normalColor));
    }

    private SelectedCallBack selectedCallBack;

    /**
     * 新消息提醒颜色
     */
    private Color noticeColor = new Color(0xFF8C00);

    /**
     * 有新消息提醒，不可以被normal化
     */
    private boolean hasNewMessage = false;

}
