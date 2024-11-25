package Windows;

import javax.swing.*;

public class EntranceB extends JFrame {
    public EntranceB() {
        super("Entrance B");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);
        EntranceBPanel panel = new EntranceBPanel();
        add(panel);
        setVisible(true);
    }
}
