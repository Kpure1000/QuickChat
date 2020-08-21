package view.listInfoView;

import function.Debug;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseListener;

public class ChatInfoNodeRender extends DefaultTreeCellRenderer {
    /**
     * 重写getTreeCellRendererComponent
     * <p>
     * 为了渲染出自己想要的样式
     * </p>
     *
     * @param tree     the receiver is being configured for
     * @param value    the value to render
     * @param sel      whether node is selected
     * @param expanded whether node is expanded
     * @param leaf     whether node is a lead node
     * @param row      row index
     * @param hasFocus whether node has focus
     * @return the {@code Component} that the renderer uses to draw the value
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        //super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        //获取元素对象
        ChatInfoNode chatInfoNode = (ChatInfoNode) value;

        //设置是否可用
        setEnabled(chatInfoNode.isOnLine());

        //设置html文本
        String text =
                "<html><font size=\"5\"style=\"color:#89FF57\">" + chatInfoNode.getHead() + "</font><br/>" +
                        "<font size=\"3\"style=\"color:#89FF57\">" + chatInfoNode.getBody() + "</font><br/></html>";

        setText(text);

        //设置图片
        ImageIcon icon = chatInfoNode.getIcon();
        if (icon != null) {
            icon.setImage(icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            this.setIcon(icon);
            setIconTextGap(10);
        }

        //设置背景颜色
        setBackground(chatInfoNode.getBackGroundColor());
        setOpaque(true);

        return this;
    }

    private MouseListener mouseListener = null;

    private Color normalColor = new Color(0xffffff);

    private Color highlightColor = new Color(0x888888);

    private String normalRGB = "000000";

    private String highlightRGB = "";

}
