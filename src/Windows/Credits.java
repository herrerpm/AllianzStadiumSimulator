package Windows;

import javax.swing.*;
import java.awt.*;

public class Credits extends JFrame {
    public Credits(){
        setTitle("Credits");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        CreditsPanel panel = new CreditsPanel();
        setSize(550,600);
        add(panel);
        setVisible(true);
    }
}
