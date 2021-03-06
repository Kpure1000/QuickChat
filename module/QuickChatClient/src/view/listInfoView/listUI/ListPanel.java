package view.listInfoView.listUI;

import function.Debug;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 列表容器
 */
public class ListPanel extends JPanel {

    /**
     * 构造
     */
    public ListPanel() {
        gridLayout = new GridLayout(0, 1);
        this.setLayout(gridLayout);
        this.setBackground(new Color(0xefefef));
    }

    /**
     * 按行数量构造
     *
     * @param rowsCount 初始行数量
     */
    public ListPanel(int rowsCount) {
        gridLayout = new GridLayout(rowsCount, 1);
        this.setLayout(gridLayout);
    }

    /**
     * 设置高光
     *
     * @param lastID
     */
    public void setLastSelectedCell(BigInteger lastID) {
        if (lastID != null) {
            for (FriendListCell item :
                    listCells) {
                item.setHighlight(item.getID().compareTo(lastID) == 0);
            }
        }
        this.lastID = lastID;
    }

    public BigInteger getLastID() {
        return lastID;
    }

    public void RemoveAllCell() {
        this.removeAll();
        listCells.clear();
        updateLayout();
    }

    /**
     * 尾插元素
     *
     * @param listCell 元素
     */
    public void insertCell(FriendListCell listCell) {
        this.add(listCell);
        listCells.add(listCell);
        updateLayout();
    }

    /**
     * 按照索引插入
     *
     * @param index    索引
     * @param listCell 元素
     */
    public void insertCell(int index, FriendListCell listCell) {
        if (index >= 0 && index < this.getComponents().length) {
            this.add(listCell, index);
        }
        if (index >= 0 && index < listCells.size()) {
            listCells.add(index, listCell);
        }
        updateLayout();
    }

    /**
     * 按照排序插入
     *
     * @param listCell       元素
     * @param cellComparable 比较方法
     */
    public void insertCellSorted(ListCell listCell, Comparable<FriendListCell> cellComparable) {
        this.add(listCell);
        updateLayout();
    }

    public void noticeCell(BigInteger id, String newMsg) {
        int index = 0;
        for (var cell :
                listCells) {
            if (cell.getID().compareTo(id) == 0) {
                cell.setCellMessage(newMsg);
                cell.setText(cell.getFormatText());
                cell.setNotice(true);
                this.getComponents()[index] = cell;
            }
            index++;
        }
        updateUI();
    }

    /**
     * 删除元素
     *
     * @param listCell 元素
     */
    public void removeCell(ListCell listCell) {
        listCells.remove(listCell);
        this.remove(listCell);
        updateLayout();
    }

    /**
     * 对元素进行排序
     *
     * @param cellComparable 比较方法
     */
    public void sortListCell(Comparator<ListCell> cellComparable) {
        listCells.sort(cellComparable);
        this.removeAll();
        for (ListCell listCell : listCells) {
            this.add(listCell);
        }
        //直接刷新组件
        this.updateUI();
    }

    /**
     * 更新布局（主要是更新行数）
     */
    private void updateLayout() {
        gridLayout.setRows(Math.max(MinRowNumbers, listCells.size()));
        this.updateUI();
    }

    /**
     * 网格布局器
     */
    private GridLayout gridLayout;

    private final int MinRowNumbers = 10;

    /**
     * 获取元素列表
     *
     * @return
     */
    public CopyOnWriteArrayList<FriendListCell> getListCells() {
        return listCells;
    }

    /**
     * 设置元素列表
     *
     * @param listCells
     */
    public void setListCells(CopyOnWriteArrayList<FriendListCell> listCells) {
        this.listCells = listCells;
        updateLayout();
    }

    /**
     * 装有列表元素的真实列表
     */
    private CopyOnWriteArrayList<FriendListCell> listCells = new CopyOnWriteArrayList<>();

    /**
     * 上一个被选中的单元
     */
    private BigInteger lastID;

}
