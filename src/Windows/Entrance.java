package Windows;

import javax.swing.*;
import java.awt.BorderLayout;

public class Entrance extends JFrame {
    public Entrance(String name, JPanel threads){
        super(name);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);

        // Use JLayeredPane to layer EntrancePanel and threads panel
        JLayeredPane layeredPane = getLayeredPane();

        // Initialize and add the EntrancePanel at the default layer
        EntrancePanel background = new EntrancePanel();
        background.setBounds(0, 0, 800, 600);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // Configure the threads panel
        threads.setOpaque(false); // Ensure transparency
        threads.setBounds(0, 0, 800, 600);
        layeredPane.add(threads, JLayeredPane.PALETTE_LAYER); // Add above the background

        setVisible(true);
    }
}
