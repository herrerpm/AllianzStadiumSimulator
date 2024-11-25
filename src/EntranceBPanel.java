import javax.swing.*;
import java.awt.*;

public class EntranceBPanel extends JPanel {
    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        // Entrance B
        g.setColor(Color.YELLOW);
        g.fillRect(500, 0, 300, 300);
        g.setColor(Color.BLACK);
        g.drawString("Entrada B",603,15);
        // Taquillero
        g.setColor(Color.CYAN);
        g.fillRect(500, 150, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Taquilla", 503, 165);
        // Registro
        g.setColor(Color.WHITE);
        g.fillRect(650, 150, 150, 150);
        g.setColor(Color.BLACK);
        g.drawString("Registro", 653, 165);
        // Baños
        g.setColor(Color.BLUE);
        g.fillRect(0,0,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Baños", 0, 15);
        // Comida
        g.setColor(Color.ORANGE);
        g.fillRect(250,0,250,100);
        g.setColor(Color.BLACK);
        g.drawString("Comida", 253, 15);
        //Zona General
        g.setColor(Color.GRAY);
        g.fillRect(0,100,500,200);
        g.setColor(Color.BLACK);
        g.drawString("Zona General", 3, 115);
    }
}
