package Windows;

import Utils.ZoneCoordinates;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EntrancePanel extends JPanel {
    private Clip clip;

    public EntrancePanel() {
        loadSound("src/Music/Estadio de fútbol - Sonido ambiental.wav");
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        // Draw zones with their boundaries for debugging
        drawZone(g, Color.ORANGE, "Entrada", ZoneCoordinates.ENTRANCE_ZONE);
        drawZone(g, Color.CYAN, "Taquilla", ZoneCoordinates.TICKETS_ZONE);
        drawZone(g, Color.DARK_GRAY, "Registro", ZoneCoordinates.REGISTER_ZONE);
        drawZone(g, new Color(128, 64, 0), "Baños", ZoneCoordinates.BATHROOM_ZONE);
        drawZone(g, new Color(125, 33, 129), "Comida", ZoneCoordinates.FOOD_ZONE);
        drawZone(g, Color.WHITE, "Zona General", ZoneCoordinates.GENERAL_ZONE);
        drawZone(g, Color.PINK, "Gradas", ZoneCoordinates.STANDS_ZONE);
        drawZone(g, new Color(45, 87, 44), "Cancha", ZoneCoordinates.FIELD_ZONE);
        drawZone(g, Color.LIGHT_GRAY, "Banca", ZoneCoordinates.BENCH_ZONE);
    }

    private void drawZone(Graphics g, Color color, String label, Rectangle zone) {
        g.setColor(color);
        g.fillRect(zone.x, zone.y, zone.width, zone.height);
        g.setColor(Color.BLACK);
        g.drawRect(zone.x, zone.y, zone.width, zone.height); // Draw boundary
        g.drawString(label, zone.x + 3, zone.y + 15);
    }

    private void loadSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playSound() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
