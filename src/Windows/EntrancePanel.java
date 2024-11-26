package Windows;

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
        g.setColor(new Color(45,87,44));
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