package Windows;

import javax.swing.*;
import java.awt.*;

public class EntrancePanel extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        // Entrance
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 300, 250);
        g.setColor(Color.BLACK);
        g.drawString("Entrada",3,15);
        // Taquillero
        g.setColor(Color.CYAN);
        g.fillRect(0, 150, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Taquilla", 3, 165);
        // Registro
        g.setColor(Color.WHITE);
        g.fillRect(150, 150, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Registro", 153, 165);
        // Baños
        g.setColor(Color.BLUE);
        g.fillRect(300,0,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Baños", 303, 15);
        // Comida
        g.setColor(Color.ORANGE);
        g.fillRect(550,0,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Comida", 553, 15);
        //Zona General
        g.setColor(Color.GRAY);
        g.fillRect(300,100,500,200);
        g.setColor(Color.BLACK);
        g.drawString("Zona General", 303, 115);
        // Gradas
        g.setColor(Color.PINK);
        g.fillRect(0,250,800,325);
        g.setColor(Color.BLACK);
        g.drawString("Gradas",3,265);
        // Cancha
        g.setColor(Color.GREEN);
        g.fillRect(100,300,600,250);
        g.setColor(Color.BLACK);
        g.drawString("Cancha", 103, 315);
        // Banca
        g.setColor(Color.WHITE);
        g.fillRect(150,550,500,25);
        g.setColor(Color.BLACK);
        g.drawString("Banca",153, 565);
    }
}