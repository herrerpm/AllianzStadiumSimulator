import javax.swing.*;

public class SimulationWindow extends JFrame {
    public SimulationWindow(){
        super("Simulation Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);
        SimulationWindowPanel panel = new SimulationWindowPanel();
        add(panel);
        setVisible(true);
    }
}