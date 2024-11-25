import javax.swing.*;

public class EntranceB extends JFrame {
    public EntranceB() {
        setTitle("Entrance B");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,300);
        EntranceBPanel panel = new EntranceBPanel();
        add(panel);
        setVisible(true);
    }
}
