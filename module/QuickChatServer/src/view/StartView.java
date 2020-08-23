package view;

import function.Debug;
import function.StartServer;
import view.listUI.ListPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 不一定需要GUI
 */
@Deprecated
public class StartView extends JFrame {
    public StartView() {
        this.setVisible(true);
        this.setTitle("服务器");
        this.setSize(100, 100);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        startServer = new StartServer();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                startServer.CloseServer();
                Debug.Log("服务器已关闭");
                super.windowClosed(e);
                System.exit(0);
            }
        });
    }

    StartServer startServer;
}
