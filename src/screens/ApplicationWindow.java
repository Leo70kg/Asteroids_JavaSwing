package screens;

import javax.swing.*;

public class ApplicationWindow extends JFrame {
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 1000;

    public ApplicationWindow(String name) {
        super(name);
        this.setVisible(true);
        this.setBounds(300,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setFocusable(true);
        this.requestFocus();
    }

}

