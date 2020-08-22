package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import data.DataManager;
import function.ChatManager;
import network.ClientNetwork;
import view.listInfoView.ChatInfoNode;
import view.listInfoView.ChatInfoNodeRender;
import view.listInfoView.listUI.FriendListCell;
import view.listInfoView.listUI.ListCell;
import view.listInfoView.listUI.ListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 聊天主页
 */
public class ChatView {
    private JPanel panel1;
    private JEditorPane editorPane1;
    private JButton BT_Info;
    private JButton BT_History;
    private JTextArea textArea1;
    private JPanel topSettingPanel;
    private JButton BT_AddFriend;
    private JButton BT_AddGroup;
    private JButton BT_Setting;
    private JScrollPane JSP_FriendList;
    /**
     * 一定要注意，这个必须要检查字符串长度，否则会导致问题
     */
    private JLabel LB_ChatObjTitle;
    private JButton BT_Quit;
    private JPanel quitPanel;
    private JPanel settingPanel;
    private JPanel topPanel;
    private JButton BT_MyInfo;
    private JButton BT_Emoji;
    private JTree tree1;
    private ChatInfoNode root;
    private Point pressedPoint;
    private ListPanel friendList;

    public ChatView() {
        JFrame asd = new JFrame();
//        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
//        try {
//            UIManager.setLookAndFeel(lookAndFeel);
//        } catch (ClassNotFoundException |
//                InstantiationException |
//                IllegalAccessException |
//                UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
        $$$setupUI$$$();
//        asd.setUndecorated(true);
        asd.setVisible(true);
        asd.setSize(800, 600);
        asd.setLocationRelativeTo(null);
//        asd.setResizable(false);
        asd.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        asd.getContentPane().setBackground(new Color(0x999999));
        asd.setTitle("聊天");

        asd.add($$$getRootComponent$$$());

        //好友列表数////////////////////////
//        Color back = new Color(0x3d3f41);
//        root = new ChatInfoNode(back, "我的好友", "");
//        for (int i = 0; i < 15; i++) {
//            ChatInfoNode sub = new ChatInfoNode(back, new ImageIcon("image/h1.jpg"),
//                    "好友" + (i + 1), "你好你好你好你好", i % 2 == 0, false);
//            root.add(sub);
//        }
//        tree1 = new JTree(root);
//        tree1.setBackground(back);
//        tree1.setDropMode(DropMode.ON);
//        tree1.setOpaque(true);
//        JSP_FriendList.setViewportView(tree1);
//        //将树的前面连接去掉
//        tree1.putClientProperty("JTree.lineStyle", "Horizontal");
//        //设置树的字体大小，样式
//        tree1.setFont(new Font("微软雅黑", Font.PLAIN, 1));
//        //设置树节点的高度
//        tree1.setRowHeight(70);
//        //设置单元渲染描述
//        tree1.setCellRenderer(new ChatInfoNodeRender());
//        tree1.setRootVisible(false);
//        tree1.addTreeSelectionListener(e -> {
//                    ChatInfoNode curNode = ((ChatInfoNode)
//                            tree1.getLastSelectedPathComponent());
//                    LB_ChatObjTitle.setText("<html><font size=\"5\" style = \"color:#89FF57\">" + curNode.getHead() + "</font></html>");
//                }
//        );
        ////////////////////////////////

        //好友列表
        friendList = new ListPanel();
        for (int i = 0; i < 20; i++) {
            friendList.insertCell(new FriendListCell(
                            new ImageIcon("image/h1.jpg"), "好友" + (i + 1),
                            "富强民主文明和谐", friendListCell -> {
                        friendList.setLastSelectedCell(friendListCell);
                        LB_ChatObjTitle.setText(friendListCell.getFormatName());
                    })
            );
        }
        ((GridLayout) friendList.getLayout()).setVgap(5);
        JSP_FriendList.setViewportView(friendList);

        LB_ChatObjTitle.setText("<html><font size=\"5\" style = \"color:#89FF57\">请选择聊天对象</font></html>");

        BT_AddFriend.setBackground(new Color(0x89FF57));

        BT_Quit.addActionListener(e -> {
            asd.dispose();
        });

        BT_Info.addActionListener(e -> {
            // TODO 弹出显示信息的窗口
        });

        BT_AddFriend.addActionListener(e -> {
            // TODO 弹出好友申请列表的窗口
        });

        asd.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { //鼠标按下事件
                pressedPoint = e.getPoint(); //记录鼠标坐标
            }
        });
        asd.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) { // 鼠标拖拽事件
                Point point = e.getPoint();// 获取当前坐标
                Point locationPoint = asd.getLocation();// 获取窗体坐标
                int x = locationPoint.x + point.x - pressedPoint.x;// 计算移动后的新坐标
                int y = locationPoint.y + point.y - pressedPoint.y;
                asd.setLocation(x, y);// 改变窗体位置
            }
        });

        //重写关闭
        asd.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // TODO 聊天窗口的关闭意味着客户端关闭，暂时这么写
                chatManager.Close();
                super.windowClosed(e);
            }
        });

        chatManager = new ChatManager();

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setContinuousLayout(false);
        splitPane1.setDividerLocation(250);
        splitPane1.setEnabled(true);
        splitPane1.setOneTouchExpandable(false);
        splitPane1.setResizeWeight(0.5);
        panel1.add(splitPane1, BorderLayout.CENTER);
        JSP_FriendList = new JScrollPane();
        JSP_FriendList.setBackground(new Color(-11513259));
        JSP_FriendList.setForeground(new Color(-12763327));
        splitPane1.setLeftComponent(JSP_FriendList);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(panel2);
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setDividerLocation(319);
        splitPane2.setLastDividerLocation(300);
        splitPane2.setOrientation(0);
        splitPane2.setResizeWeight(0.5);
        panel2.add(splitPane2, BorderLayout.CENTER);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        splitPane2.setRightComponent(panel3);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel4.setBackground(new Color(-1379847));
        panel3.add(panel4, BorderLayout.NORTH);
        BT_Emoji = new JButton();
        BT_Emoji.setBackground(new Color(-9273713));
        BT_Emoji.setForeground(new Color(-1379847));
        BT_Emoji.setHorizontalAlignment(0);
        BT_Emoji.setHorizontalTextPosition(0);
        BT_Emoji.setText("表情");
        BT_Emoji.setVerticalAlignment(0);
        panel4.add(BT_Emoji);
        BT_History = new JButton();
        BT_History.setBackground(new Color(-9273713));
        BT_History.setForeground(new Color(-1379847));
        BT_History.setHorizontalAlignment(0);
        BT_History.setHorizontalTextPosition(0);
        BT_History.setText("历史记录");
        BT_History.setVerticalAlignment(0);
        panel4.add(BT_History);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, BorderLayout.CENTER);
        textArea1 = new JTextArea();
        textArea1.setBackground(new Color(-1379847));
        Font textArea1Font = this.$$$getFont$$$("Microsoft YaHei", -1, 18, textArea1.getFont());
        if (textArea1Font != null) textArea1.setFont(textArea1Font);
        textArea1.setForeground(new Color(-16777216));
        scrollPane1.setViewportView(textArea1);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        splitPane2.setLeftComponent(panel5);
        final JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setHorizontalScrollBarPolicy(31);
        scrollPane2.setVerticalScrollBarPolicy(20);
        panel5.add(scrollPane2, BorderLayout.CENTER);
        editorPane1 = new JEditorPane();
        editorPane1.setBackground(new Color(-11513259));
        editorPane1.setEditable(false);
        editorPane1.setForeground(new Color(-7733417));
        scrollPane2.setViewportView(editorPane1);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel6.setBackground(new Color(-12763327));
        panel5.add(panel6, BorderLayout.NORTH);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.setBackground(new Color(-12763327));
        panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        LB_ChatObjTitle = new JLabel();
        LB_ChatObjTitle.setText("聊天对象名称");
        panel7.add(LB_ChatObjTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel8.setBackground(new Color(-12763327));
        panel6.add(panel8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        BT_Info = new JButton();
        BT_Info.setBackground(new Color(-7733417));
        BT_Info.setForeground(new Color(-12763327));
        BT_Info.setHorizontalAlignment(0);
        BT_Info.setHorizontalTextPosition(0);
        BT_Info.setText("查看资料");
        BT_Info.setVerticalAlignment(0);
        panel8.add(BT_Info);
        final Spacer spacer1 = new Spacer();
        panel6.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.setBackground(new Color(-11513259));
        panel1.add(topPanel, BorderLayout.NORTH);
        settingPanel = new JPanel();
        settingPanel.setLayout(new BorderLayout(0, 0));
        settingPanel.setBackground(new Color(-11513259));
        topPanel.add(settingPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        topSettingPanel = new JPanel();
        topSettingPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        topSettingPanel.setBackground(new Color(-11513259));
        settingPanel.add(topSettingPanel, BorderLayout.WEST);
        BT_MyInfo = new JButton();
        BT_MyInfo.setAutoscrolls(true);
        BT_MyInfo.setBackground(new Color(-7733417));
        BT_MyInfo.setForeground(new Color(-12763327));
        BT_MyInfo.setHideActionText(false);
        BT_MyInfo.setHorizontalAlignment(0);
        BT_MyInfo.setOpaque(true);
        BT_MyInfo.setSelected(false);
        BT_MyInfo.setText("我的资料");
        topSettingPanel.add(BT_MyInfo);
        BT_AddFriend = new JButton();
        BT_AddFriend.setAutoscrolls(true);
        BT_AddFriend.setBackground(new Color(-7733417));
        BT_AddFriend.setForeground(new Color(-12763327));
        BT_AddFriend.setHideActionText(false);
        BT_AddFriend.setHorizontalAlignment(0);
        BT_AddFriend.setOpaque(true);
        BT_AddFriend.setSelected(false);
        BT_AddFriend.setText("添加好友");
        topSettingPanel.add(BT_AddFriend);
        BT_AddGroup = new JButton();
        BT_AddGroup.setAutoscrolls(true);
        BT_AddGroup.setBackground(new Color(-7733417));
        BT_AddGroup.setForeground(new Color(-12763327));
        BT_AddGroup.setHideActionText(false);
        BT_AddGroup.setHorizontalAlignment(0);
        BT_AddGroup.setOpaque(true);
        BT_AddGroup.setSelected(false);
        BT_AddGroup.setText("添加群聊");
        topSettingPanel.add(BT_AddGroup);
        BT_Setting = new JButton();
        BT_Setting.setAutoscrolls(true);
        BT_Setting.setBackground(new Color(-7733417));
        BT_Setting.setForeground(new Color(-12763327));
        BT_Setting.setHideActionText(false);
        BT_Setting.setHorizontalAlignment(0);
        BT_Setting.setOpaque(true);
        BT_Setting.setSelected(false);
        BT_Setting.setText("设置");
        topSettingPanel.add(BT_Setting);
        quitPanel = new JPanel();
        quitPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        quitPanel.setBackground(new Color(-11513259));
        topPanel.add(quitPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        BT_Quit = new JButton();
        BT_Quit.setBackground(new Color(-7733417));
        BT_Quit.setForeground(new Color(-12763327));
        BT_Quit.setText("  退出  ");
        quitPanel.add(BT_Quit, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        quitPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }


    ChatManager chatManager;

    String chatContent = "";

}
