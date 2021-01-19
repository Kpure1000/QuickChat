package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import data.DataManager;
import data.MessageContent;
import data.ServerInfo;
import data.UserManager;
import function.ChatManager;
import function.ChatManagerCallBack;
import function.Debug;
import message.ServerMessage;
import message.UserMessage;
import network.ClientNetwork;
import network.ListenerCallBack;
import network.ListenerCallBackAdapter;
import view.listInfoView.listUI.FriendListCell;
import view.listInfoView.listUI.ListCell;
import view.listInfoView.listUI.ListPanel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 聊天主页
 */
public class ChatView {
    private JPanel panel1;
    private JEditorPane editorPane1;
    /**
     * chat object info
     */
    private JButton BT_Info;
    private JButton BT_SendFile;
    private JTextArea textInput;
    private JPanel topSettingPanel;
    private JButton BT_AddFriend;
    private JButton BT_AddGroup;
    /**
     * 设置，目前只有修改密码功能
     */
    private JButton BT_Setting;
    private JScrollPane JSP_FriendList;
    /**
     * 一定要注意，这个必须要检查字符串长度，否则会导致问题
     */
    private JLabel LB_ChatObjTitle;
    private JPanel quitPanel;
    private JPanel settingPanel;
    private JPanel topPanel;
    /**
     * self info
     */
    private JButton BT_MyInfo;
    private JButton BT_Emoji;
    private JLabel topLabel;
    private JLabel LB_Self; //  TODO 自己的ID
    private JScrollPane jsp_Text;
    //    private JTree tree1;
//    private ChatInfoNode root;
    private Point pressedPoint;
    private ListPanel friendList;

    //////// system tray
    private SystemTray systemTray;
    private TrayIcon trayIcon;

    JFrame chatFrame;

    public ChatView() {
        chatFrame = new JFrame();
//        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
//        try {
//            UIManager.setLookAndFeel(lookAndFeel);
//        } catch (ClassNotFoundException |
//                InstantiationException |
//                IllegalAccessException |
//                UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }
        chatFrame.setVisible(true);
        chatFrame.setSize(800, 600);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chatFrame.getContentPane().setBackground(new Color(0x999999));
        chatFrame.setTitle("聊天");

        chatFrame.add($$$getRootComponent$$$());

        InitTray();

        LB_Self.setText("<html><font size=\"5\" style = \"color:#89FF57\">" +
                UserManager.getInstance().getUserInfo().getID().toString()
                + "</font></html>");


        //好友列表///////////////////////

        friendList = new ListPanel();
        ((GridLayout) friendList.getLayout()).setVgap(5);
        JSP_FriendList.setViewportView(friendList);
        LB_ChatObjTitle.setText(DefaultChatObjectName);

        editorPane1.setEditorKit(new HTMLEditorKit());
        editorPane1.setContentType("text/html");

        //// BUTTON ////

        BT_MyInfo.addActionListener(e -> {
            friendList.sortListCell(compByNotice);
            chatManager.requireList();
        });

        BT_AddFriend.addActionListener(e -> {
        });
        BT_AddFriend.setEnabled(false);

        BT_AddGroup.addActionListener(e -> {
        });
        BT_AddGroup.setEnabled(false);

        BT_Info.addActionListener(e -> {
            //  TODO 显示对象的信息
        });

        BT_Setting.addActionListener(e -> {
            //  TODO 显示自己信息
        });

        BT_SendFile.addActionListener(e -> {
            //  TODO 发送文件
            FileSystemView fileSystemView = FileSystemView.getFileSystemView();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(fileSystemView.getHomeDirectory());
            fileChooser.setDialogTitle("选择要发送的文件");
            fileChooser.setApproveButtonText("确定");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int res = fileChooser.showOpenDialog(chatFrame);
            File targetFile = new File("");
            if (JFileChooser.APPROVE_OPTION == res) {
                targetFile = fileChooser.getSelectedFile();
                Debug.Log("文件: " + targetFile.getName() + ", 大小: " + targetFile.getUsableSpace());
            }
            finalTargetFile = targetFile;
            //  添加发送文件的监听

            //  请求发送文件
            if (chatManager.getCurChatObject().compareTo(new BigInteger("9999")) != 0) {
                ClientNetwork.getInstance().sendMessage(new UserMessage(
                        UserMessage.MessageType.Require_SendPrivateFile,
                        UserManager.getInstance().getUserInfo().getID(),
                        chatManager.getCurChatObject(),
                        targetFile.getName() + "#" + targetFile.getUsableSpace()
                ));
            } else {
                ClientNetwork.getInstance().sendMessage(new UserMessage(
                        UserMessage.MessageType.Require_SendGroupFile,
                        UserManager.getInstance().getUserInfo().getID(),
                        chatManager.getCurChatObject(),
                        targetFile.getName() + "#" + targetFile.getUsableSpace()
                ));
            }
        });

        ClientNetwork.getInstance().addListenerCallBack(new ListenerCallBackAdapter() {
            @Override
            public ListenerCallBack OnAllowSendFile(ServerMessage serverMessage) {
                //  收到这个反馈时，服务器已经打开了文件服务端口监听，只需直接发送即可
                //  发送文件
                Debug.Log("收到允许发送的反馈，开始发送文件");
                var curServer = DataManager.getInstance().getCurrentServer();
                new Thread(() -> {
                    int tryCounts = 0;
                    while (tryCounts < 10) {
                        try {
                            Socket fileSocket = new Socket(curServer.getHost(), 18888);
                            //  文件读入流
                            Debug.Log("（发送端）文件服务器连接成功");
                            FileInputStream fileInputStream = new FileInputStream(finalTargetFile);
                            OutputStream outputStream = fileSocket.getOutputStream();
                            byte[] bytes = new byte[512];
                            int len = 0;
                            while ((len = fileInputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, len);
                            }
                            outputStream.flush();
                            outputStream.close();
                            fileInputStream.close();
                            fileSocket.close();
                            Debug.Log("文件: " + finalTargetFile.getName() + "发送完成");
                            return;
                        } catch (IOException ioException) {
                            Debug.LogError("文件服务器连接失败.");
                            tryCounts++;
                            ioException.printStackTrace();
                            try {
                                //  等一会儿再连
                                Thread.sleep(800);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    }
                }).start();
                return super.OnAllowSendFile(serverMessage);
            }
        });

        BT_Emoji.addActionListener(e -> {
            //  TODO 选择表情
        });

        /////////

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
            public void windowClosing(WindowEvent e) {
                //  是否直接退出
                chatFrame.getToolkit().beep();
                int isClose = new QuitDialog(chatFrame).showQuitDialog();
                if (isClose == 1) {
                    chatFrame.dispose();
                } else if (isClose == 2) {
                    if (chatFrame.isVisible())
                        chatFrame.setVisible(false);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                chatManager.Close();
                super.windowClosed(e);
                System.exit(0);
            }
        });

        chatManager = new ChatManager();

        chatManager.setChatManagerCallBack(new ChatManagerCallBack() {
            @Override
            public void OnForceClose() {
                RecoveryFrame(chatFrame);
                JOptionPane.showMessageDialog(chatFrame, "强制下线通知", "强制下线通知", JOptionPane.WARNING_MESSAGE);
                chatFrame.dispose();
            }

            @Override
            public void OnSendMessageSuccess(UserMessage userMessage) {
                chatContent += formatMessageFromClient(userMessage);
                editorPane1.setText(chatContent);
            }

            @Override
            public void OnReceivePrivateMsg(ServerMessage serverMessage) {
                chatContent += formatMessageFromServer(serverMessage);
                editorPane1.setText(chatContent);
                Point p = new Point();
                p.setLocation(0, editorPane1.getHeight() * 10);
                jsp_Text.getViewport().setViewPosition(p);
            }

            @Override
            public void OnReceiveGroupMsg(ServerMessage serverMessage) {
                chatContent += formatMessageFromServer(serverMessage);
                editorPane1.setText(chatContent);
                Point p = new Point();
                p.setLocation(0, editorPane1.getHeight() * 10);
                jsp_Text.getViewport().setViewPosition(p);
            }

            @Override
            public void OnReceiveTestMsg(ServerMessage serverMessage) {
                chatContent += formatMessageFromServer(serverMessage);
                editorPane1.setText(chatContent);
                Point p = new Point();
                p.setLocation(0, editorPane1.getHeight() * 10);
                jsp_Text.getViewport().setViewPosition(p);
            }

            @Override
            public void OnMakeNotice(ServerMessage serverMessage) {
                //  如果: 窗口失焦，或者未选中聊天对象，或者发送者不是聊天对象
                if (!chatFrame.isFocused() ||
                        chatManager.getCurChatObject() == null ||
                        chatManager.getCurChatObject().compareTo(serverMessage.getSenderID()) == 0) {
                    //  发送系统提醒
                    OnNewMessageIn(serverMessage.getSenderID().toString(), serverMessage.getContent());
                }
            }

            @Override
            public void OnReceiveOnLineList(ArrayList<BigInteger> idList) {
                //  清空原有列表
                friendList.RemoveAllCell();
                //  获取列表
                boolean curChatObjectIsOnline = false; //  当前聊天对象是否仍然在线
                //  在头添加群聊
                idList.add(0, new BigInteger("9999"));
                //  遍历在线列表
                for (var item :
                        idList) {
                    if (item.compareTo(UserManager.getInstance().getUserInfo().getID()) != 0) { //  不包括自己
                        //  获取与这个聊天对象有关的消息记录
                        var record = DataManager.getInstance().getMessageRecord(item);
                        //  消息缓存
                        CopyOnWriteArrayList<MessageContent> recList = new CopyOnWriteArrayList<>();
                        if (record != null) {
                            recList = record.getMessageContents();
                        }
                        //  获取Top消息
                        String topMsg = (recList.size() > 0 ? recList.get(recList.size() - 1).getContent() : "无消息");
                        //  根据ID构建新的列表Cell
                        String name = (item.compareTo(new BigInteger("9999")) == 0 ? "群聊" : item.toString());
                        FriendListCell newCell = new FriendListCell(
                                new ImageIcon("image/h1.jpg"),
                                item,                               //  ID
                                name,                               //  Name
                                topMsg,                             //  Top消息
                                friendListCell -> {                 //  cell被选择时候的回调
                                    //  设置聊天对象的名字（聊天区域顶部）
                                    LB_ChatObjTitle.setText(friendListCell.getFormatName());
                                    //  设置目前聊天对象的ID
                                    chatManager.setCurChatObject(friendListCell.getID());
                                    //  设置高光
                                    friendList.setLastSelectedCell(friendListCell.getID());
                                    //  清空消息显示内容
                                    //  TODO 这样效率可能较低，可能要考虑把内存中所有读取过的字符串记录放在一个池中
                                    chatContent = "";
                                    if (record != null) {
                                        Debug.Log("刷新记录框，记录: " + record.getChatObjectID()
                                                + "记录数量: " + record.getMessageContents().size());
                                        StringBuilder chatContentBuilder = new StringBuilder();
                                        var curID = UserManager.getInstance().getUserInfo().getID();
                                        for (var content :
                                                record.getMessageContents()) {
                                            if (content.getSenderID().compareTo(curID) == 0) {
                                                //  如果是自己发的
                                                chatContentBuilder.append(
                                                        formatMessageFromClient(content.getSendTime(), content.getSenderID(),
                                                                content.getReceiverID(), content.getContent()));
                                            } else {
                                                //  如果是接收到的
                                                chatContentBuilder.append(
                                                        formatMessageFromServer(content.getSendTime(),
                                                                content.getSenderID(), content.getReceiverID(),
                                                                content.getContent()));
                                            }
                                        }
                                        chatContent += chatContentBuilder.toString();
                                    }
                                    editorPane1.setText(chatContent);
                                    if (chatManager.getCurChatObject() != null &&
                                            chatManager.getCurChatObject().compareTo(new BigInteger("9999")) != 0) {
                                        //  有聊天对象，启用功能
                                        BT_Info.setEnabled(true);
                                        BT_SendFile.setEnabled(true);
                                    }
                                }             //  cell被选择时候的回调
                        );
                        newCell.setColor(new Color(0xE5E5E5),
                                new Color(0x9C9C9C));
                        newCell.setEnabled(true);
                        newCell.setNotice(false);
                        //加入列表
                        friendList.insertCell(newCell);
                        if (chatManager.getCurChatObject() != null &&
                                item.compareTo(chatManager.getCurChatObject()) == 0) {
                            curChatObjectIsOnline = true;
                        }
                    }
                }
                //  设置高光为上一次被选中的那个
                if (friendList.getLastID() != null) {
                    friendList.setLastSelectedCell(friendList.getLastID());
                } else {
                    friendList.setLastSelectedCell(null);
                }
                if (!curChatObjectIsOnline) {
                    //  如果当前对象下线，则清空聊天对象以及窗口
                    chatContent = "";
                    //  设置聊天对象的名字（聊天区域顶部）
                    LB_ChatObjTitle.setText(DefaultChatObjectName);
                    //  设置目前聊天对象的ID
                    chatManager.setCurChatObject(null);
                    editorPane1.setText(chatContent);
                    Point p = new Point();
                    p.setLocation(0, editorPane1.getHeight() * 10);
                    jsp_Text.getViewport().setViewPosition(p);
                }
                if (chatManager.getCurChatObject() == null ||
                        chatManager.getCurChatObject().compareTo(new BigInteger("9999")) == 0) {
                    //  无聊天对象，禁用功能
                    BT_Info.setEnabled(false);
                    BT_SendFile.setEnabled(false);
                }
            }

            @Override
            public void OnRequireSendFile(ServerMessage serverMessage) {
                //  不是自己发的文件请求，可以确认
                if (serverMessage.getSenderID().compareTo(
                        UserManager.getInstance().getUserInfo().getID()) != 0) {
                    //  弹窗是否接收文件
                    Debug.Log("有人要传输文件");
                    int res = JOptionPane.showConfirmDialog(chatFrame, "是否接收文件?",
                            "文件传输确认", JOptionPane.YES_NO_OPTION);
                    if (res == JOptionPane.YES_OPTION) {
                        //  允许
                        ClientNetwork.getInstance().sendMessage(new UserMessage(
                                UserMessage.MessageType.Reply_ReceiveFile,
                                serverMessage.getSenderID(),
                                serverMessage.getReceiverID(),
                                serverMessage.getContent()
                        ));
                    } else {
                        //  不允许
                        ClientNetwork.getInstance().sendMessage(new UserMessage(
                                UserMessage.MessageType.Reply_ReceiveFile,
                                serverMessage.getSenderID(),
                                null,
                                serverMessage.getContent()
                        ));
                    }
                }
            }

            @Override
            public void OnReceiveFile(ServerMessage serverMessage) {
                //  不是自己发的文件，可以接收
                if (serverMessage.getSenderID().compareTo(
                        UserManager.getInstance().getUserInfo().getID()) != 0) {
                    //  开始接收文件
                    Debug.Log("被允许接收文件");
                    new Thread(() -> {
                        String fileName = serverMessage.getContent().split("#")[0];
                        int tryCounts = 0;
                        while (tryCounts < 10) {
                            try {
                                ServerInfo curServer = DataManager.getInstance().getCurrentServer();
                                Socket fileSocket = new Socket(curServer.getHost(), 18888);
                                Debug.Log("（接收端）文件服务器连接成功");
                                // 将文件内容从硬盘读入内存中
                                InputStream socketIn = fileSocket.getInputStream();
                                OutputStream outputStream = fileSocket.getOutputStream();
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(socketIn);
                                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                                        new FileOutputStream(
                                                FileSystemView.getFileSystemView().getHomeDirectory()
                                                        + "/Downloads/" + fileName));
                                // 定义每次接收文件的大小
                                byte[] buffer = new byte[512];
                                int len = 0;
                                // 因为文件内容较大，不能一次完毕，因此需要通过循环来分次发送
                                while ((len = bufferedInputStream.read(buffer)) != -1) {
                                    bufferedOutputStream.write(buffer, 0, len);
                                }
                                bufferedOutputStream.flush();
                                bufferedOutputStream.close();
                                bufferedInputStream.close();
                                outputStream.close();
                                socketIn.close();
                                fileSocket.close();
                                Debug.Log("文件: " + fileName + "接收完毕");
                                return;
                            } catch (IOException e) {
                                Debug.LogError("文件服务器连接失败.");
                                tryCounts++;
                                e.printStackTrace();
                                try {
                                    //  等一会儿再连
                                    Thread.sleep(800);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });


        //  发送消息
        textInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (KeyEvent.VK_ENTER == e.getKeyChar()) {
                    var inputStr = textInput.getText();
                    if (inputStr.trim().length() > 0 && !inputStr.equals("\n")) {
                        inputStr = inputStr.replace('\n', '\0');
                        chatManager.sendMessage(inputStr);
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

    private void InitTray() {
        if (SystemTray.isSupported()) {
            //  获取系统托盘
            systemTray = SystemTray.getSystemTray();
            //  加载图片
            Image trayImg = Toolkit.getDefaultToolkit().getImage("image/l1.jpg");
            //  创建托盘目录
            PopupMenu trayMenu = new PopupMenu();

            MenuItem openMenu = new MenuItem("Open");

            MenuItem exitMenu = new MenuItem(">Exit>");
            openMenu.addActionListener(e -> {
                //  显示窗口
                RecoveryFrame(chatFrame);
            });
            exitMenu.addActionListener(e -> {
                //  退出程序
                chatFrame.dispose();
            });
            trayMenu.add(openMenu);
            trayMenu.add(exitMenu);

            //  系统托盘图标
            trayIcon = new TrayIcon(trayImg, "双击返回聊天", trayMenu);

            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            //  显示窗口
                            RecoveryFrame(chatFrame);
                        }
                    }
                    super.mouseClicked(e);
                }
            });
            try {
                //  添加系统托盘图标到托盘
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }

        } else {
            Debug.LogError("System Tray is not supported in this SHIT OS");
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        panel4.setBackground(new Color(-2039067));
        panel3.add(panel4, BorderLayout.NORTH);
        BT_Emoji = new JButton();
        BT_Emoji.setHorizontalAlignment(0);
        BT_Emoji.setHorizontalTextPosition(0);
        BT_Emoji.setText("表情");
        BT_Emoji.setVerticalAlignment(0);
        panel4.add(BT_Emoji);
        BT_SendFile = new JButton();
        BT_SendFile.setHorizontalAlignment(0);
        BT_SendFile.setHorizontalTextPosition(0);
        BT_SendFile.setText("发送文件");
        BT_SendFile.setVerticalAlignment(0);
        panel4.add(BT_SendFile);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, BorderLayout.CENTER);
        textInput = new JTextArea();
        textInput.setBackground(new Color(-1052689));
        Font textInputFont = this.$$$getFont$$$("Microsoft YaHei", -1, 18, textInput.getFont());
        if (textInputFont != null) textInput.setFont(textInputFont);
        textInput.setForeground(new Color(-16777216));
        scrollPane1.setViewportView(textInput);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        splitPane2.setLeftComponent(panel5);
        jsp_Text = new JScrollPane();
        jsp_Text.setHorizontalScrollBarPolicy(31);
        jsp_Text.setVerticalScrollBarPolicy(20);
        panel5.add(jsp_Text, BorderLayout.CENTER);
        editorPane1 = new JEditorPane();
        editorPane1.setBackground(new Color(-11513259));
        editorPane1.setEditable(false);
        editorPane1.setForeground(new Color(-7733417));
        jsp_Text.setViewportView(editorPane1);
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
        BT_MyInfo.setHideActionText(false);
        BT_MyInfo.setHorizontalAlignment(0);
        BT_MyInfo.setText("我的信息");
        topSettingPanel.add(BT_MyInfo);
        BT_AddFriend = new JButton();
        BT_AddFriend.setHideActionText(false);
        BT_AddFriend.setHorizontalAlignment(0);
        BT_AddFriend.setText("添加好友");
        topSettingPanel.add(BT_AddFriend);
        BT_AddGroup = new JButton();
        BT_AddGroup.setHideActionText(false);
        BT_AddGroup.setHorizontalAlignment(0);
        BT_AddGroup.setText("添加群聊");
        topSettingPanel.add(BT_AddGroup);
        BT_Setting = new JButton();
        BT_Setting.setHideActionText(false);
        BT_Setting.setHorizontalAlignment(0);
        BT_Setting.setText("设置");
        topSettingPanel.add(BT_Setting);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel9.setBackground(new Color(-11513259));
        settingPanel.add(panel9, BorderLayout.CENTER);
        LB_Self = new JLabel();
        LB_Self.setText("我自己");
        panel9.add(LB_Self, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

    private void OnNewMessageIn(String fromWhat, String content) {
        trayIcon.displayMessage("您有一条新的消息", "From '" + fromWhat + "': " + content, TrayIcon.MessageType.INFO);
    }

    /**
     * Recovery frame from tray, minimize or other focus
     *
     * @param frame main frame
     */
    private void RecoveryFrame(JFrame frame) {
        //  from tray
        if (!frame.isVisible())
            frame.setVisible(true);
        //  from iconified
        frame.setState(JFrame.NORMAL);
        //  from other focus
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

    private String formatMessageFromServer(ServerMessage serverMessage) {
        return formatMessageFromServer(serverMessage.getFeedbackTime(), serverMessage.getSenderID(),
                serverMessage.getReceiverID(), serverMessage.getContent());
    }

    private String formatMessageFromServer(Date feedbackTime, BigInteger senderID, BigInteger receiverID, String content) {
        String ret;
        if (chatManager.getCurChatObject().compareTo(new BigInteger("9999")) == 0) {
            ret = "<div align=\"left\" style=\"color:#aaaaaa\"><font size=\"4\">" +
                    "<i>(" + timeFormat.format(feedbackTime) + ") </i>  " +
                    "<b>" + senderID + " 说:" + "</b><br/>" +
                    "<font size=\"5\" style=\"color:#ffffff\">" + content + "</font>" +
                    "</font></div><br/>";
        } else {
            ret = "<div align=\"left\" style=\"color:#aaaaaa\"><font size=\"4\">" +
                    "<i>(" + timeFormat.format(feedbackTime) + ") </i>  " +
                    "<b>" + senderID + " 对 " + receiverID + " 说:" + "</b><br/>" +
                    "<font size=\"5\" style=\"color:#ffffff\">" + content + "</font>" +
                    "</font></div><br/>";
        }
        return ret;
    }

    private String formatMessageFromClient(UserMessage userMessage) {
        return formatMessageFromClient(new Date(), userMessage.getSenderID(), userMessage.getReceiverID(), userMessage.getContent());
    }

    private String formatMessageFromClient(Date feedbackTime, BigInteger senderID, BigInteger receiverID, String content) {
        String ret;
        if (chatManager.getCurChatObject().compareTo(new BigInteger("9999")) == 0) {
            ret = "<div align=\"right\" style=\"color:#aaaaaa\"><font size=\"4\">" +
                    "<i>(" + timeFormat.format(feedbackTime) + ") </i>  " +
                    "<b>" + senderID + " 说:" + "</b><br/>" +
                    "<font size=\"5\" style=\"color:#ffffff\">" + content + "</font>" +
                    "</font></div><br/>";
        } else {
            ret = "<div align=\"right\" style=\"color:#aaaaaa\"><font size=\"4\">" +
                    "<i>(" + timeFormat.format(feedbackTime) + ") </i>  " +
                    "<b>" + senderID + " 对 " + receiverID + " 说:" + "</b><br/>" +
                    "<font size=\"5\" style=\"color:#ffffff\">" + content + "</font>" +
                    "</font></div><br/>";
        }
        return ret;
    }

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    private ChatManager chatManager;

    private String chatContent = "";

    private final String DefaultChatObjectName = "<html><font size=\"5\" style = \"color:#89FF57\">请选择聊天对象</font></html>";

    private File finalTargetFile;

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
