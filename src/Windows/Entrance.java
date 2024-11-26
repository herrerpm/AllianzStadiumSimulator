package Windows;

import javax.swing.*;
import Managers.GraphicsManager;

import java.awt.*;

public class Entrance extends JFrame {
    public Entrance(String name, JPanel threads){
        super(name);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800,600);
        EntrancePanel background = new EntrancePanel();
        add(background, BorderLayout.CENTER);
        threads.setOpaque(false);
        add(threads, BorderLayout.CENTER);
        setVisible(true);
    }
}