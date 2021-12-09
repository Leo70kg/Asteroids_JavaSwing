package screens;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutScreen extends JPanel {

    JButton b1, b2;

    public AboutScreen()
    {
        b1 = new JButton("Menu");
        b2 = new JButton("Exit");

        this.add(b1);
        this.add(b2);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuListener.getInstance().setHasPressedMenu(true);
            }
        });

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuListener.getInstance().setHasPressedExit(true);
            }
        });

    }


}
