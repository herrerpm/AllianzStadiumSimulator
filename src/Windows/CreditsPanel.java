package Windows;

import javax.swing.*;
import java.awt.*;

public class CreditsPanel extends JPanel {
    private Image background;
    public CreditsPanel() {
        background = new ImageIcon("src/Images/UP.jpg").getImage();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 75, 220, 400, 300, this);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));

        int startY = 30; // Posición inicial en Y
        int lineHeight = 30; // Espacio entre líneas
        // Logo UP
        // Carrera
        // Materia
        // Estudiantes
        // Profesor
        // Fecha
        String [] credits= {
                "Credits:",
                "Career: Computer Graphics and System Engineering",
                "Class: Fundamentals of Parallel Programming",
                "Students: Gabriel Guerra, Miguel Herrera,",
                "               Diego Jimenez & Daniel Prado",
                "Professor: Dr. Juan Carlos López Pimentel",
                "Date: 27 of November 2024"
        };

        for (String line : credits) {
            g.drawString(line, 30, startY);
            startY += lineHeight;
        }
    }
}