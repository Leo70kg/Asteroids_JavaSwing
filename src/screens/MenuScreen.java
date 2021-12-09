package screens;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JPanel {

    JButton b1, b2, b3, b4;

    public MenuScreen()
    {
        b1 = new JButton("New Game");
        b2 = new JButton("About");
        b3 = new JButton("High Score");
        b4 = new JButton("Exit");

        this.add(b1);
        this.add(b2);
        this.add(b3);
        this.add(b4);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuListener.getInstance().setHasPressedNewGame(true);
            }
        });

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuListener.getInstance().setHasPressedAboutScreen(true);
            }
        });

        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuListener.getInstance().setHasPressedHighScoreScreen(true);
            }
        });

        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuListener.getInstance().setHasPressedExit(true);
            }
        });
    }

}
