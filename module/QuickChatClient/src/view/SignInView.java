package view;

import data.DataManager;
import function.Debug;
import function.SignIn;
import function.SignInCallBack;
import network.ClientNetwork;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class SignInView extends JFrame {

    //  UI
    private JTextField idText;
    private JComboBox<BigInteger> idBox;

    private JPasswordField passText;
    private JCheckBox passCheck;
    private JLabel errorLabel;

    private final int MyLightRgb = 0x89FF57;
    private final int MyDarkRgb = 0x3d3f41;
    ////

    public SignInView() {
        int topY = 220;
        int leftX = 110;


        //窗体初设置
        this.setSize(700, 525);
        this.setLocationRelativeTo(null);// 窗体居中
        this.getContentPane().setBackground(new Color(MyDarkRgb));
        //功能标题
        JLabel title = new JLabel("登陆");
        title.setBounds(this.getWidth()/2-170,60,350,100);
        title.setFont(new Font("微软雅黑",Font.PLAIN,48));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(MyLightRgb));
        this.add(title);
        //第零层
        JLabel closeLabel = new JLabel("X");
        closeLabel.setBackground(new Color(MyDarkRgb));
        closeLabel.setForeground(new Color(MyLightRgb));
        closeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        closeLabel.setBounds(665, 15, 30, 30);
        this.add(closeLabel);
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    SignInView.this.dispose();
                    DataManager.getInstance().Close();
                    ClientNetwork.getInstance().Disconnect();
                }
            }
        });
        //第一层
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(new Color(MyLightRgb));
        idLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        idLabel.setBounds(leftX, topY, 80, 30);
        this.add(idLabel);
        idText = new JTextField();
        idText.setForeground(new Color(MyDarkRgb));
        idText.setBackground(new Color(MyLightRgb));
        idText.setBounds(leftX + 30 + 40, topY, 280, 30);
        this.add(idText);
        idBox = new JComboBox<BigInteger>();
        idBox.setForeground(new Color(MyDarkRgb));
        idBox.setBackground(new Color(MyLightRgb));
        idBox.setBounds(leftX + 30 + 40 + 280 + 10, topY, 110, 30);
        this.add(idBox);

        //第二层
        JLabel passLabel = new JLabel("密码:");
        passLabel.setForeground(new Color(MyLightRgb));
        passLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));
        passLabel.setBounds(leftX, topY + 50 + 50, 80, 30);
        this.add(passLabel);
        passText = new JPasswordField();
        passText.setForeground(new Color(MyDarkRgb));
        passText.setBackground(new Color(MyLightRgb));
        passText.setBounds(leftX + 30 + 40, topY + 50 + 50, 400, 30);
        this.add(passText);
        errorLabel = new JLabel("");
        errorLabel.setBackground(new Color(MyDarkRgb));
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("微软雅黑", Font.BOLD | Font.ITALIC, 13));
        errorLabel.setBounds(leftX + 30 + 40 + 400 + 10, topY + 50 + 50, 200, 30);
        this.add(errorLabel);
        //第三层
        JLabel passConfigText = new JLabel("记住密码");
        passConfigText.setForeground(new Color(MyLightRgb));
        passConfigText.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        passConfigText.setBounds(leftX, topY + 140, 60, 30);
        this.add(passConfigText);
        passCheck = new JCheckBox();
        passCheck.setBackground(new Color(MyDarkRgb));
        passCheck.setBounds(leftX + 67, topY + 145, 20, 20);
        this.add(passCheck);
        //第四层
        JButton signInButton = new JButton("登录");
        signInButton.setBackground(new Color(MyLightRgb));
        signInButton.setForeground(new Color(MyDarkRgb));
        signInButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        signInButton.setBounds(leftX + 100, topY + 200, 90, 30);
        this.add(signInButton);
        JButton signUpButton = new JButton("注册");
        signUpButton.setBackground(new Color(MyLightRgb));
        signUpButton.setForeground(new Color(MyDarkRgb));
        signUpButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        signUpButton.setBounds(leftX + 100 + 200, topY + 200, 90, 30);
        this.add(signUpButton);

        //窗体
        this.setUndecorated(true);// 取消窗体修饰效果
        this.getContentPane().setLayout(null);// 窗体使用绝对布局
        this.setAlwaysOnTop(true); //窗体最顶层显示
        this.setLayout(null);//绝对布局
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        /*-------------------------------------------------------*/
        //初始化登录功能
        signIn = new SignIn();

        //设置登录功能回调，并加载ID数据
        signIn.setSignInCallBack(new SignInCallBack() {
            @Override
            public void OnGetIDConfig(ArrayList<BigInteger> ids) {
                if (ids != null && ids.size() > 0) {
                    Debug.Log("正在将ID记录写入ComboBox...");
                    for (var item :
                            ids) {
                        idBox.addItem(item);
                    }
                    //默认第一个是
                    idText.setText(ids.get(0).toString());
                    signIn.getPasswordConfig(ids.get(0).toString());
                }
            }

            @Override
            public void OnGetPassConfig(String password) {
                Debug.Log("正字获取密保设置:"+password);
                if (password != null) {
                    //勾选记住密码
                    passCheck.setSelected(true);
                    passTmp = password;
                    passText.setText(passTmp);
                } else {
                    //取消记住密码
                    passText.setText("");
                    passCheck.setSelected(false);
                }
            }

            @Override
            public void OnIDInputError() {
                errorLabel.setText("ID格式错误(纯数字)");
            }

            @Override
            public void OnPassInputError() {
                errorLabel.setText("密码格式错误(6-16)");
                passText.setText("");
            }

            @Override
            public boolean OnNeedPassConfigUpdate() {
                // TODO 更新密码配置，不知道isSelected能不能用
                return passCheck.isSelected();
            }

            @Override
            public void OnSignInSuccess() {
                // TODO 关闭自身，开启下一个窗口
                Debug.Log("登录成功!");
                SignInView.this.dispose();
                new ChatView();
            }

            @Override
            public void OnSignInFailed() {
                // TODO登陆失败，清空密码输入框
                Debug.LogWarning("登陆失败，清空密码框");
                errorLabel.setText("账户或密码错误");
                passText.setText("");
            }
        });

        //ID选框变化（该ID一定存在于记录中），获取该ID的记住密码配置
        idBox.addItemListener(e -> {
            Debug.Log("更新输入框");
            //更新输入框
            idText.setText(e.getItem().toString());
            //根据历史ID获取密码配置
            signIn.getPasswordConfig(e.getItem().toString());
        });

        //密码记住框 变动
        passCheck.addItemListener(e -> {
            // TODO 更新密码配置，不知道isSelected能不能用
            Debug.Log("记住密码选框发生变化");
            signIn.setPasswordConfig(idText.getText(), passCheck.isSelected());
        });

        //登录按钮监听
        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SignInAction();
            }
        });

        //注册按钮
        signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO 隐藏此窗口，打开注册界面，这里需要注册ID自动填充的回调
                SignInView.this.setVisible(false);
                //打开注册功能界面，并添加回调
                new SignUpView(SignInView.this).setSignUpViewCallBack((newID, password) -> {
                    // TODO自动填充
                    Debug.Log("注册后自动填充！");
                    SignInView.this.idText.setText(newID.toString());
                    SignInView.this.passText.setText(password);
                });
            }
        });

        passText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignInAction();
                }
            }
        });
        idText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignInAction();
                }
            }
        });
        idBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignInAction();
                }
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

        //重写关闭方法
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Debug.Log("关闭了登录窗口");
                // TODO 关闭所有进程之前释放功能资源
                if (signIn != null) {
                    signIn.Close();
                }
                SignInView.super.dispose();
            }
        });
    }

    /**
     * 登录动作
     */
    private void SignInAction() {
        passTmp = new String(passText.getPassword());
        if (signIn != null) {
            signIn.inputInformation(idText.getText(), passTmp);
        }
    }

//    /**
//     * ID列表
//     */
//    private ArrayList<BigInteger> idList;

    /**
     * 密码缓存
     */
    private String passTmp;

    /**
     * 鼠标坐标
     */
    Point pressedPoint;

    /**
     * 登录功能
     */
    SignIn signIn;
}
