package view;

import function.SignIn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class SignInView extends JFrame {

    //  UI
    private JTextField idText;
    private JComboBox<BigInteger> idBox;

    private JPasswordField passText;
    private JLabel errorLabel;

    private final int MyLightRgb = 0x89FF57;
    private final int MyDarkRgb = 0x3d3f41;
    ////

    public SignInView() {
        int topY = 220;
        int leftX = 110;
        //第零层
        JLabel closeLabel = new JLabel("X");
        closeLabel.setBackground(new Color(MyDarkRgb));
        closeLabel.setForeground(new Color(MyLightRgb));
        closeLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 20));
        closeLabel.setBounds(665, 15, 30, 30);
        this.add(closeLabel);
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.exit(0);
                }
            }
        });
        //第一层
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(new Color(MyLightRgb));
        idLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 22));
        idLabel.setBounds(leftX, topY, 80, 30);
        idText = new JTextField();
        idText.setForeground(new Color(MyDarkRgb));
        idText.setBackground(new Color(MyLightRgb));
        idText.setBounds(leftX + 30 + 40, topY, 280, 30);
        idBox = new JComboBox<BigInteger>();
        idBox.setForeground(new Color(MyDarkRgb));
        idBox.setBackground(new Color(MyLightRgb));
        idBox.setBounds(leftX + 30 + 40 + 280 + 10, topY, 110, 30);
        this.add(idLabel);
        this.add(idText);
        this.add(idBox);
        //第二层
        JLabel passLabel = new JLabel("密码:");
        passLabel.setForeground(new Color(MyLightRgb));
        passLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 22));
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
        JButton signInButton = new JButton("登录");
        signInButton.setBackground(new Color(MyLightRgb));
        signInButton.setForeground(new Color(MyDarkRgb));
        signInButton.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 20));
        signInButton.setBounds(leftX + 100, topY + 200, 90, 30);
        this.add(signInButton);
        JButton signUpButton = new JButton("注册");
        signUpButton.setBackground(new Color(MyLightRgb));
        signUpButton.setForeground(new Color(MyDarkRgb));
        signUpButton.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 20));
        signUpButton.setBounds(leftX + 100 + 200, topY + 200, 90, 30);
        this.add(signUpButton);
        //窗体
        this.setUndecorated(true);// 取消窗体修饰效果
        this.getContentPane().setLayout(null);// 窗体使用绝对布局
        //this.setAlwaysOnTop(true); //窗体最顶层显示
        this.setTitle("登录");
        this.setLayout(null);//绝对布局
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(800, 400, 700, 525);
        this.setLocationRelativeTo(null);// 窗体居中
        this.getContentPane().setBackground(new Color(MyDarkRgb));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        /*-------------------------------------------------------*/
        //初始化登录功能
        SignIn signIn = new SignIn();
        signIn.setCallBack(new SignIn.SignInCallBack() {
            @Override
            public void OnGetIDConfig(ArrayList<BigInteger> ids) {
                for (var item :
                        ids) {
                    idBox.addItem(item);
                }
            }

            @Override
            public void OnGetPassConfig(String password) {
                passTmp = password;
            }

            @Override
            public void OnReceiveFeedBack(boolean feedback) {
                if (feedback) {
                    // TODO 登录成功
                }
            }
        });

        idBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                idText.setText(e.getItem().toString());
            }
        });

        //登录按钮监听
        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SignInAction(signIn);
            }
        });

        passText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignInAction(signIn);
                }
            }
        });
        idText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignInAction(signIn);
                }
            }
        });
        idBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SignInAction(signIn);
                }
            }
        });
    }

    /**
     * 登录事件内容
     *
     * @param signIn
     */
    void SignInAction(SignIn signIn) {
        if (idText.getText() != null && idText.getText().length() > 0) {
            if (passText.getPassword().length >= 6) {
                errorLabel.setText("");
                // TODO 这里要加一个判断ID输入是否合法的方法
                signIn.inputInformation(new BigInteger(idText.getText()),
                        String.valueOf(passText.getPassword()));
            } else {
                errorLabel.setText("密码不少于6位");
                passText.setText("");
            }
        } else {
            errorLabel.setText("ID输入/选择有误");
        }
    }

    private ArrayList<BigInteger> idList;
    private String passTmp;
    Point pressedPoint;
}
