package view.listUI;

import javax.swing.*;
import java.awt.*;

/**
 * 列表单元
 */
public class ListCell extends JLabel {

    public ListCell() {
        setEnabled(true);
        setOpaque(true);
        setHighlight(false);
    }

    /**
     * 设置Label的背景颜色
     *
     * @param color
     */
    public void setColor(Color color) {
        setBackground(color);
    }

    /**
     * 绑定状态颜色
     *
     * @param normal    普通状态颜色
     * @param highlight 高亮（选中）状态颜色
     */
    public void setColor(Color normal, Color highlight) {
        this.normalColor = normal;
        this.highlightColor = highlight;
    }

    public Color getNormalColor() {
        return normalColor;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * 设置选中高光
     *
     * @param selected 是否被选中
     */
    public void setHighlight(boolean selected) {
        setBackground(selected ? highlightColor : normalColor);
    }

    protected Color normalColor = new Color(0xE5E5E5);

    protected Color highlightColor = new Color(0xA8A8A8);
}
