package view;

import data.ServerInfo;
import function.Debug;
import function.Welcome;
import network.ClientNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 欢迎界面，选择服务器
 *
 * @see ClientNetwork
 * @see data.DataManager
 * @see SignInView
 */
public class WelcomeView extends JFrame {

    //欢迎界面功能
    private final Welcome welcome;

    /**
     * 缓存的地址
     */
    private String host;

    /**
     * 缓存的端口
     */
    private int port;

    private Point pressedPoint;

    public WelcomeView() {
        //窗体初设置
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);// 窗体居中
        this.getContentPane().setBackground(new Color(MyDarkRgb));
        //第零行
        JLabel closeLabel = new JLabel("X");
        closeLabel.setBackground(new Color(MyDarkRgb));
        closeLabel.setForeground(new Color(MyLightRgb));
        closeLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 16));
        closeLabel.setBounds(375, 15, 30, 30);
        this.add(closeLabel);
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //关掉自己
                    WelcomeView.this.dispose();
                }
            }
        });
        JLabel titleL = new JLabel("欢迎使用QuickChat");
        titleL.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 24));
        titleL.setBounds(this.getBounds().width / 2 - 130, 40, 260, 40);
        titleL.setForeground(new Color(MyLightRgb));
        this.add(titleL);
        //第一行
        JLabel hostT = new JLabel("服务器列表");
        hostT.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 18));
        hostT.setForeground(new Color(MyLightRgb));
        hostT.setBounds(topX, topY, 110, 26);
        this.add(hostT);
        hostCombo = new JComboBox<>();
        hostCombo.setForeground(new Color(MyDarkRgb));
        hostCombo.setBackground(new Color(MyLightRgb));
        hostCombo.setBounds(topX + 110 + 10, topY, 180, 26);
        this.add(hostCombo);

        // TODO 第二行
        errorLabel = new JLabel("服务器连接错误");
        errorLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(this.getBounds().width / 2 - 75, topY + 50, 160, 30);
        this.add(errorLabel);
        // TODO 第三行
        JButton confirm = new JButton("确定");
        confirm.setForeground(new Color(MyDarkRgb));
        confirm.setBackground(new Color(MyLightRgb));
        confirm.setBounds(topX + 200, topY + 140, 80, 30);
        this.add(confirm);

        // 连接服务器 按钮事件
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 连接服务器
                welcome.ConnectToServer(host, port);
            }
        });

        //窗体最后设置
        this.setUndecorated(true);// 取消窗体修饰效果
        this.getContentPane().setLayout(null);// 窗体使用绝对布局
        this.setAlwaysOnTop(true); //窗体最顶层显示
        this.setLayout(null);//绝对布局
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        //选项变动时的
        hostCombo.addItemListener(e -> {
            host = ((ServerInfo)e.getItem()).getHost();
            port = ((ServerInfo)e.getItem()).getPort();
        });

        //欢迎功能
        welcome = new Welcome();

        welcome.setWelcomeCallBack(new Welcome.WelcomeCallBack() {
            @Override
            public void OnGetServerList(ArrayList<ServerInfo> serverList) {
                if (serverList == null || serverList.size() <= 0) {
                    return;
                }
                for (ServerInfo item :
                        serverList) {
                    //添加服务器选项
                    hostCombo.addItem(item);
                }
                //默认为第一个服务器
                host = serverList.get(0).getHost();
                port = serverList.get(0).getPort();
            }

            @Override
            public void OnConnectSuccess() {
                // TODO 连接成功
                Debug.Log("连接成功");
                //创建登录窗口
                new SignInView();
                //关掉自己
                WelcomeView.this.dispose();
            }
        });


        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { //鼠标按下事件
                pressedPoint = e.getPoint(); //记录鼠标坐标
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) { // 鼠标拖拽事件
                Point point = e.getPoint();// 获取当前坐标
                Point locationPoint = getLocation();// 获取窗体坐标
                int x = locationPoint.x + point.x - pressedPoint.x;// 计算移动后的新坐标
                int y = locationPoint.y + point.y - pressedPoint.y;
                setLocation(x, y);// 改变窗体位置
            }
        });

        //重写关闭动作
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                Debug.Log("关闭了欢迎窗口");
                welcome.Close();
                super.windowClosed(e);
            }
        });
    }

    //UI
    private final int MyLightRgb = 0x89FF57;
    private final int MyDarkRgb = 0x3d3f41;
    private int topX = 50, topY = 120;

    /**
     * 选项框
     */
    private JComboBox<ServerInfo> hostCombo;

    private JLabel errorLabel;
}
