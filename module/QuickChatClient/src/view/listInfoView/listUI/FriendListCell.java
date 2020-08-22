package view.listInfoView.listUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 好友列表单元
 *
 * @see ListCell
 */
public class FriendListCell extends ListCell {

    /**
     * 构造好友列表单元，这已经是最底层的实现了
     * @param icon 图标
     * @param name 好友名称
     * @param message 最新消息
     * @param selectedCallBack 被点击选择的回调接口
     */
    public FriendListCell(ImageIcon icon, String name, String message,SelectedCallBack selectedCallBack) {

        super();

        this.cellIcon = icon;
        this.cellName = name;
        this.cellMessage = message;
        this.selectedCallBack=selectedCallBack;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedCallBack.OnSelected(FriendListCell.this);
            }
        });

        cellIcon.setImage(cellIcon.getImage().getScaledInstance(70,70,Image.SCALE_DEFAULT));
        this.setIcon(cellIcon);

        this.setText(getFormatText());

        this.setIconTextGap(10);

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

    public String getFormatText() {
        return "<html><font size=\"4\" style = \"color:#000000\">" + cellName + "</font><br/>" +
                "<font size=\"3\" style = \"color:#000000\">" + cellMessage + "</font></html>";
    }

    public String getFormatName(){
        return "<html><font size=\"5\" style = \"color:#89FF57\">" + cellName + "</font></html>";
    }

    public void setCellIcon(ImageIcon cellIcon) {
        this.cellIcon = cellIcon;
    }

    private String cellName;

    private String cellMessage;

    private ImageIcon cellIcon;

    public interface SelectedCallBack {
        public void OnSelected(FriendListCell friendListCell);
    }

    private SelectedCallBack selectedCallBack;

}
