package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import function.ChatManager;
import function.ChatManagerCallBack;
import function.Debug;
import message.ServerMessage;
import view.listInfoView.listUI.FriendListCell;
import view.listInfoView.listUI.ListCell;
import view.listInfoView.listUI.ListPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * 聊天主页
 */
public class ChatView {
    private JPanel panel1;
    private JEditorPane editorPane1;
    private JButton BT_Info;
    private JButton BT_History;
    private JTextArea textInput;
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
    private JLabel topLabel;
    //    private JTree tree1;
//    private ChatInfoNode root;
    private Point pressedPoint;
    private ListPanel friendList;

    public ChatView() {
        JFrame chatFrame = new JFrame();
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
//        chatFrame.setUndecorated(true);
        chatFrame.setVisible(true);
        chatFrame.setSize(800, 600);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        chatFrame.getContentPane().setBackground(new Color(0x999999));
        chatFrame.setTitle("聊天");

        chatFrame.add($$$getRootComponent$$$());

        //好友列表///////////////////////

        friendList = new ListPanel();
        for (int i = 0; i < 20; i++) {
            FriendListCell newCell = new FriendListCell(
                    new ImageIcon("image/h1.jpg"), new BigInteger(Integer.toString(i)), "好友" + (i + 1),
                    "富强民主文明和谐", friendListCell -> {
                friendList.setLastSelectedCell(friendListCell);
                LB_ChatObjTitle.setText(friendListCell.getFormatName());
                chatManager.SelectChatObject(friendListCell.getID());
            });
            newCell.setColor(new Color(0xE5E5E5),
                    new Color(0x9C9C9C));
            newCell.setEnabled(i % 3 == 0);
            newCell.setNotice(i % 4 == 0);
            //加入列表
            friendList.insertCell(newCell);
        }
        ((GridLayout) friendList.getLayout()).setVgap(5);
        JSP_FriendList.setViewportView(friendList);
        LB_ChatObjTitle.setText("<html><font size=\"5\" style = \"color:#89FF57\">请选择聊天对象</font></html>");

        BT_MyInfo.addActionListener(e -> friendList.sortListCell(compByNotice));

        BT_AddFriend.addActionListener(e -> friendList.sortListCell(compByEnable));

        ////////////////////////////////

        BT_AddFriend.setBackground(new Color(0x89FF57));

        BT_Info.addActionListener(e -> {
            // TODO 弹出显示信息的窗口
        });

        BT_AddFriend.addActionListener(e -> {
            // TODO 弹出好友申请列表的窗口
        });

        chatFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { //鼠标按下事件
                pressedPoint = e.getPoint(); //记录鼠标坐标
            }
        });
        chatFrame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) { // 鼠标拖拽事件
                Point point = e.getPoint();// 获取当前坐标
                Point locationPoint = chatFrame.getLocation();// 获取窗体坐标
                int x = locationPoint.x + point.x - pressedPoint.x;// 计算移动后的新坐标
                int y = locationPoint.y + point.y - pressedPoint.y;
                chatFrame.setLocation(x, y);// 改变窗体位置
            }
        });

        //重写关闭
        chatFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // TODO 聊天窗口的关闭意味着客户端关闭，暂时这么写
                chatManager.Close();
                super.windowClosed(e);
            }
        });

        chatManager = new ChatManager();

        chatManager.setChatManagerCallBack(new ChatManagerCallBack() {
            @Override
            public void OnForceClose() {
                chatFrame.dispose();
            }

            @Override
            public void OnReceivePrivateMsg(ServerMessage serverMessage) {
                chatContent += "\nPrivate: " + serverMessage.getContent();
                editorPane1.setText(chatContent);
            }

            @Override
            public void OnReceiveGroupMsg(ServerMessage serverMessage) {
                chatContent += "\nGroup: " + serverMessage.getContent();
                editorPane1.setText(chatContent);
            }

            @Override
            public void OnReceiveTestMsg(ServerMessage serverMessage) {
                chatContent += "\nTest: " + serverMessage.getContent();
                editorPane1.setText(chatContent);
            }

            @Override
            public void OnReceiveOnLineList(ArrayList<BigInteger> idList) {
                friendList.removeAll();
                Debug.Log("获取到列表:");
                for (var item :
                        idList) {
                    Debug.Log("好友: " + item);
                    FriendListCell newCell = new FriendListCell(
                            new ImageIcon("image/h1.jpg"), item, item.toString(),
                            "富强民主文明和谐", friendListCell -> {
                        friendList.setLastSelectedCell(friendListCell);
                        LB_ChatObjTitle.setText(friendListCell.getFormatName());
                        chatManager.SelectChatObject(friendListCell.getID());
                    });
                    newCell.setColor(new Color(0xE5E5E5),
                            new Color(0x9C9C9C));
                    newCell.setEnabled(true);
                    newCell.setNotice(false);
                    //加入列表
                    friendList.insertCell(newCell);
                }
            }
        });

        textInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyChar()) {
                    if (textInput.getText().length() > 0 && !textInput.getText().equals("\n")) {
                        chatManager.sendMessage(textInput.getText());
                        textInput.setText("");
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
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
        splitPane2.setDividerLocation(324);
        splitPane2.setLastDividerLocation(240);
        splitPane2.setOrientation(0);
        splitPane2.setResizeWeight(0.4);
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
        textInput = new JTextArea();
        textInput.setBackground(new Color(-1379847));
        Font textInputFont = this.$$$getFont$$$("Microsoft YaHei", -1, 18, textInput.getFont());
        if (textInputFont != null) textInput.setFont(textInputFont);
        textInput.setForeground(new Color(-16777216));
        scrollPane1.setViewportView(textInput);
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
        settingPanel.add(topSettingPanel, BorderLayout.EAST);
        BT_MyInfo = new JButton();
        BT_MyInfo.setAutoscrolls(true);
        BT_MyInfo.setHideActionText(false);
        BT_MyInfo.setHorizontalAlignment(0);
        BT_MyInfo.setOpaque(true);
        BT_MyInfo.setSelected(false);
        BT_MyInfo.setText("更新列表");
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
        quitPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        quitPanel.setBackground(new Color(-11513259));
        topPanel.add(quitPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        topLabel = new JLabel();
        topLabel.setText("");
        quitPanel.add(topLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

    /**
     * 比较-优先提醒
     */
    Comparator<ListCell> compByNotice = (o1, o2) ->
            ((FriendListCell) o1).isHasNewMessage() &&
                    !((FriendListCell) o2).isHasNewMessage() ? -1 : 0;

    /**
     * 比较-优先在线
     */
    Comparator<ListCell> compByEnable = (o1, o2) ->
            (o1).isEnabled() && !(o2).isEnabled() ? -1 : 0;

}
