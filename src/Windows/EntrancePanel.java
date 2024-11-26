package Windows;

import javax.swing.*;
import java.awt.*;

public class EntrancePanel extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        // Entrance
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, 300, 250);
        g.setColor(Color.BLACK);
        g.drawString("Entrada",3,15);
        // Taquillero
        g.setColor(Color.CYAN);
        g.fillRect(0, 150, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Taquilla", 3, 165);
        // Registro
        g.setColor(Color.DARK_GRAY);
        g.fillRect(150, 150, 150, 150);
        g.setColor(Color.WHITE);
        g.drawString("Registro", 153, 165);
        // Baños
        g.setColor(new Color(128, 64, 0));
        g.fillRect(300,0,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Baños", 303, 15);
        // Comida
        g.setColor(new Color(125,33,129));
        g.fillRect(550,0,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Comida", 553, 15);
        //Zona General
        g.setColor(Color.WHITE);
        g.fillRect(300,100,500,200);
        g.setColor(Color.BLACK);
        g.drawString("Zona General", 303, 115);
        // Gradas
        g.setColor(Color.PINK);
        g.fillRect(0,250,400,325);
        g.setColor(Color.BLACK);
        g.drawString("Gradas",3,265);
        // Cancha
        g.setColor(new Color(45,87,44));
        g.fillRect(400,250,400,225);
        g.setColor(Color.BLACK);
        g.drawString("Cancha", 403, 265);
        // Banca
        g.setColor(Color.lightGray);
        g.fillRect(400,475,400,125);
        g.setColor(Color.BLACK);
        g.drawString("Banca",403, 490);
    }
}