import javax.swing.*;
import java.awt.*;

public class SimulationWindowPanel extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        // Entrance A
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 200, 250);
        g.setColor(Color.BLACK);
        g.drawString("Entrada A",3,15);
        // Taquillero
        g.setColor(Color.CYAN);
        g.fillRect(0, 150, 100, 150);
        g.setColor(Color.BLACK);
        g.drawString("Taquilla", 3, 165);
        // Registro
        g.setColor(Color.WHITE);
        g.fillRect(100, 150, 100, 150);
        g.setColor(Color.BLACK);
        g.drawString("Registro", 103, 165);
        // Baños
        g.setColor(Color.BLUE);
        g.fillRect(200,0,300,100);
        g.setColor(Color.BLACK);
        g.drawString("Baños", 203, 15);
        // Comida
        g.setColor(Color.YELLOW);
        g.fillRect(500,0,300,100);
        g.setColor(Color.BLACK);
        g.drawString("Comida", 503, 15);
        //Zona General
        g.setColor(Color.GRAY);
        g.fillRect(200,1000,400,200);
        g.setColor(Color.BLACK);
        g.drawString("Zona General", 203, 115);
        // Gradas
        g.setColor(Color.PINK);
        g.fillRect(0,250,800,350);
        g.setColor(Color.BLACK);
        g.drawString("Gradas",3,265);
        // Cancha
        g.setColor(Color.GREEN);
        g.fillRect(50,300,700,250);
        g.setColor(Color.BLACK);
        g.drawString("Cancha", 53, 365);
        // Banca
        g.setColor(Color.WHITE);
        g.fillRect(150,550,500,50);
        g.setColor(Color.BLACK);
        g.drawString("Banca",153, 565);
    }
}