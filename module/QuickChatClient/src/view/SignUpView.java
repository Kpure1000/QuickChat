package view;

import function.Debug;
import function.SignUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;

public class SignUpView extends JFrame {

    //  UI
    private JTextField nameText;
    private JComboBox<BigInteger> idBox;

    private JPasswordField passText;
    private JPasswordField rePassText;
    private JCheckBox passCheck;
    private JLabel errorLabel;

    private final int MyLightRgb = 0xbfbfbf; //  0x89FF57;
    private final int MyDarkRgb = 0x3d3f41;
    ////

    public SignUpView(JFrame parentFrame) {
        this.signInView = parentFrame;

        int topY = 220;
        int leftX = 90;

        //窗体初设置
        this.setSize(700, 525);
        this.setLocationRelativeTo(null);// 窗体居中
        this.getContentPane().setBackground(new Color(MyDarkRgb));
        //功能标题
        JLabel title = new JLabel("注册");
        title.setBounds(this.getWidth() / 2 - 170, 60, 350, 100);
        title.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 48));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(MyLightRgb));
        this.add(title);
        //第零层
        JLabel closeLabel = new JLabel("返回");
        closeLabel.setBackground(new Color(MyDarkRgb));
        closeLabel.setForeground(new Color(MyLightRgb));
        closeLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 20));
        closeLabel.setBounds(665, 15, 30, 30);
        this.add(closeLabel);
        //关闭按钮
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    SignUpView.this.dispose();
                    // 返回上级
                    signUpViewCallBack.OnAutoFilltoSignIn(null, null);
                }
            }
        });
        //第一层
        JLabel nameLabel = new JLabel("设置名称:");
        nameLabel.setForeground(new Color(MyLightRgb));
        nameLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 22));
        nameLabel.setBounds(leftX, topY, 100, 30);
        this.add(nameLabel);
        nameText = new JTextField();
        nameText.setForeground(new Color(MyDarkRgb));
        nameText.setBackground(new Color(MyLightRgb));
        nameText.setBounds(leftX + 100 + 10, topY, 280, 30);
        this.add(nameText);

        //第二层
        JLabel passLabel = new JLabel("设置密码:");
        passLabel.setForeground(new Color(MyLightRgb));
        passLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 22));
        passLabel.setBounds(leftX, topY + 50, 100, 30);
        this.add(passLabel);
        passText = new JPasswordField();
        passText.setForeground(new Color(MyDarkRgb));
        passText.setBackground(new Color(MyLightRgb));
        passText.setBounds(leftX + 100 + 10, topY + 50, 400, 30);
        this.add(passText);
        errorLabel = new JLabel("");
        errorLabel.setBackground(new Color(MyDarkRgb));
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("微软雅黑", Font.BOLD | Font.ITALIC, 13));
        errorLabel.setBounds(leftX + 100 + 10 + 400 + 10, topY + 50, 200, 30);
        this.add(errorLabel);
        //第三层
        JLabel rePassLabel = new JLabel("重复密码:");
        rePassLabel.setForeground(new Color(MyLightRgb));
        rePassLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 22));
        rePassLabel.setBounds(leftX, topY + 50 + 50, 100, 30);
        this.add(rePassLabel);
        rePassText = new JPasswordField();
        rePassText.setForeground(new Color(MyDarkRgb));
        rePassText.setBackground(new Color(MyLightRgb));
        rePassText.setBounds(leftX + 100 + 10, topY + 50 + 50, 400, 30);
        this.add(rePassText);
        errorLabel = new JLabel("");
        errorLabel.setBackground(new Color(MyDarkRgb));
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("微软雅黑", Font.BOLD | Font.ITALIC, 13));
        errorLabel.setBounds(leftX + 100 + 10 + 400 + 10, topY + 50 + 50, 200, 30);
        this.add(errorLabel);
        idBox = new JComboBox<>();
        //第四层
        JButton signUpButton = new JButton("注册");
        signUpButton.setBackground(new Color(MyLightRgb));
        signUpButton.setForeground(new Color(MyDarkRgb));
        signUpButton.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 20));
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
        signUp = new SignUp();

        //设置登录功能回调
        signUp.setSignUpCallBack(new SignUp.SignUpCallBack() {
            @Override
            public void OnNewIDFeedBack(BigInteger newID) {
                // TODO当注册成功，反馈了新的ID
                // 应该是弹窗提示，然后确认后进入登陆界面，自动填充新的ID
                // 具体如何实现自动填充，详见 https://github.com/Kpure1000/QuickChat/issues/3
                JOptionPane.showMessageDialog(SignUpView.this,
                        "您的新ID: " + newID.toString(), "注册成功", JOptionPane.INFORMATION_MESSAGE
                );
                // TODO回到上一登陆界面
                SignUpView.this.dispose();
                // 自动填充
                signUpViewCallBack.OnAutoFilltoSignIn(newID, repass);
            }

            @Override
            public void OnPassInputError(String errorMsg) {
                errorLabel.setText(errorMsg);
            }

        });

        //注册按钮监听
        signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO 注册功能
                SignUpAction();
            }
        });

        passText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // TODO 注册功能
                    SignUpAction();
                }
            }
        });
        nameText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // TODO 注册功能
                    SignUpAction();
                }
            }
        });
        idBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // TODO 注册功能
                    SignUpAction();
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

        //重写关闭
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Debug.Log("关闭了注册窗口");
                // TODO 关闭所有进程之前释放功能资源
                if (signUp != null) {
                    signUp.Close();
                }
                super.windowClosed(e);
            }
        });
    }

    /**
     * 注册动作
     */
    public void SignUpAction() {
        pass = new String(passText.getPassword());
        repass = new String(rePassText.getPassword());
        signUp.inputInformation(nameText.getText(), pass, repass);
    }

    Point pressedPoint;

    /**
     * 注册功能
     */
    SignUp signUp;

    private JFrame signInView;


    String pass = "";
    String repass = "";

    public interface SignUpViewCallBack {
        void OnAutoFilltoSignIn(BigInteger newID, String password);
    }

    private SignUpViewCallBack signUpViewCallBack;

    public void setSignUpViewCallBack(SignUpViewCallBack signUpViewCallBack) {
        this.signUpViewCallBack = signUpViewCallBack;
    }
}
