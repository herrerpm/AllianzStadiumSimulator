package Windows;

import javax.swing.*;
import java.awt.*;

public class EntranceBPanel extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        // Gradas
        g.setColor(Color.PINK);
        g.fillRect(0,0,800,325);
        g.setColor(Color.BLACK);
        g.drawString("Gradas",3,15);
        // Cancha
        g.setColor(Color.GREEN);
        g.fillRect(100,25,600,250);
        g.setColor(Color.BLACK);
        g.drawString("Cancha", 103, 40);
        // Banca
        g.setColor(Color.WHITE);
        g.fillRect(150,0,500,25);
        g.setColor(Color.BLACK);
        g.drawString("Banca",153, 15);
        // Entrance B
        g.setColor(Color.YELLOW);
        g.fillRect(500, 450, 300, 250);
        g.setColor(Color.BLACK);
        g.drawString("Entrada B",503,465);
        // Taquillero
        g.setColor(Color.CYAN);
        g.fillRect(500, 300, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Taquilla", 503, 315);
        // Registro
        g.setColor(Color.WHITE);
        g.fillRect(650, 300, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Registro", 653, 315);
        // Baños
        g.setColor(Color.BLUE);
        g.fillRect(0,300,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Baños", 3, 315);
        // Comida
        g.setColor(Color.ORANGE);
        g.fillRect(250,300,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Comida", 253, 315);
        //Zona General
        g.setColor(Color.GRAY);
        g.fillRect(0,400,500,200);
        g.setColor(Color.BLACK);
        g.drawString("Zona General", 3, 415);
    }
}
