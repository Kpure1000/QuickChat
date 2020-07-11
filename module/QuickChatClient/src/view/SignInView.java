package view;

import function.SignIn;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class SignInView extends JFrame {

    //  UI
    private JTextField idText;
    private JComboBox<BigInteger> idBox;

    private JPasswordField passText;
    private JLabel errorLabel;

    private JButton signInButton;
    private JButton signUpButton;


    private final int MyLightRgb = 0x89FF57;
    private final int MyDarkRgb = 0x3d3f41;
    ////

    public SignInView(){
        int topY = 250;
        int leftX = 150;
        //第一层
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(new Color(MyLightRgb));
        idLabel.setFont(new Font("宋体",Font.PLAIN,22));
        idLabel.setBounds(leftX, topY,80,30);
        idText = new JTextField();
        idText.setForeground(new Color(MyDarkRgb));
        idText.setBackground(new Color(MyLightRgb));
        idText.setBounds(leftX +30+40, topY,280,30);
        idBox = new JComboBox<BigInteger>();
        idBox.setForeground(new Color(MyDarkRgb));
        idBox.setBackground(new Color(MyLightRgb));
        idBox.setBounds(leftX +30+40+280+10, topY,110,30);
        this.add(idLabel);
        this.add(idText);
        this.add(idBox);
        //第二层
        JLabel passLabel = new JLabel("密码:");
        passLabel.setForeground(new Color(MyLightRgb));
        passLabel.setFont(new Font("宋体",Font.PLAIN,22));
        passLabel.setBounds(leftX, topY +50+50,80,30);
        passText = new JPasswordField();
        passText.setForeground(new Color(MyDarkRgb));
        passText.setBackground(new Color(MyLightRgb));
        passText.setBounds(leftX +30+40, topY +50+50,400,30);
        this.add(passLabel);
        this.add(passText);
        //第三层
        signInButton = new JButton("登录");
        signInButton.setBackground(new Color(MyLightRgb));
        signInButton.setForeground(new Color(MyDarkRgb));
        signInButton.setBounds(leftX + 100,topY + 200,160,60);
        this.add(signInButton);
        //总
        this.setTitle("登录");
        this.setLayout(null);//绝对布局
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(800,400,800,600);
        //this.setBackground(new Color(MyDarkRgb));
        this.getContentPane().setBackground(new Color(MyDarkRgb));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

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
                passTmp=password;
            }

            @Override
            public void OnReceiveFeedBack(boolean feedback) {
                if(feedback){
                    // TODO 登录成功
                }
            }
        });
    }

    private ArrayList<BigInteger> idList;
    private String passTmp;
}
