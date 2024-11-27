package Windows;

import javax.swing.*;

public class Credits extends JFrame {
    public Credits(){
        setTitle("Credits");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        CreditsPanel panel = new CreditsPanel();
        setSize(550,600);
        add(panel);
        setVisible(true);
    }
}