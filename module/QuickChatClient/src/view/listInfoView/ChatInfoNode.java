package view.listInfoView;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseListener;

public class ChatInfoNode extends DefaultMutableTreeNode {

    public ChatInfoNode(Color backGroundColor){
        this.Head="Head";
        this.Body="Body";
        this.backGroundColor=backGroundColor;
    }

    public ChatInfoNode(Color backGroundColor,String Head,String Body){
        this.Head=Head;
        this.Body=Body;
        this.backGroundColor=backGroundColor;
        this.isOnLine=true;
    }

    public ChatInfoNode(Color backGroundColor,String Head,String Body, boolean isOnLine, boolean isUnRead){
        this.Head=Head;
        this.Body=Body;
        this.isOnLine=isOnLine;
        this.isUnRead=isUnRead;
        this.backGroundColor=backGroundColor;
    }

    public ChatInfoNode(Color backGroundColor,ImageIcon icon,String Head,String Body, boolean isOnLine, boolean isUnRead){
        this.icon=icon;
        this.Head=Head;
        this.Body=Body;
        this.isOnLine=isOnLine;
        this.isUnRead=isUnRead;
        this.backGroundColor=backGroundColor;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public void setBody(String body) {
        Body = body;
    }

    public void setHead(String head) {
        Head = head;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getBody() {
        return Body;
    }

    public String getHead() {
        return Head;
    }

    public Color getBackGroundColor() {
        return backGroundColor;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public boolean isUnRead() {
        return isUnRead;
    }

    private ImageIcon icon;

    private String Head;

    private String Body;

    private boolean isOnLine;

    private boolean isUnRead;

    private Color backGroundColor;

    private MouseListener mouseListener;

}
