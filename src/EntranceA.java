import javax.swing.*;

public class EntranceA extends JFrame {
    public EntranceA(){
        super("Entrance A");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);
        EntranceAPanel panel = new EntranceAPanel();
        add(panel);
        setVisible(true);
    }
}