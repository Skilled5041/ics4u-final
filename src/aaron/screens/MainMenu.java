package aaron.screens;

import aaron.Game;
import aaron.Main;
import aaron.graphics.CustomButton;
import aaron.graphics.Screen;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu implements Screen {
    public static Clip menuLoop;

    static {
        try {
            menuLoop = AudioSystem.getClip();
            menuLoop.open(AudioSystem.getAudioInputStream(new File("resources/menu_music.wav")));
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final CustomButton playButton = new CustomButton(800, 200, "Play");
    private static final CustomButton settingsButton = new CustomButton(800, 200, "Settings");
    private static final CustomButton aboutButton = new CustomButton(800, 200, "About");
    private static final CustomButton exitButton = new CustomButton(800, 200, "Exit");
    public static BufferedImage logo;

    static {
        try {
            logo = ImageIO.read(new File("resources/logo.png"));
        } catch (IOException e) {
            logo = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        playButton.setLocation(1920 / 2 - 400, 1080 / 2 - 450);
        settingsButton.setLocation(1920 / 2 - 400, 1080 / 2 - 200);
        aboutButton.setLocation(1920 / 2 - 400, 1080 / 2 + 50);
        exitButton.setLocation(1920 / 2 - 400, 1080 / 2 + 300);
        playButton.onClick(e -> Main.game.switchScreen(Game.Screens.CHART_SELECT));
        settingsButton.onClick(e -> Main.game.switchScreen(Game.Screens.SETTINGS));
        aboutButton.onClick(e -> Main.game.switchScreen(Game.Screens.ABOUT));
        exitButton.onClick(e -> {
            Main.game.onExit();
            System.exit(0);
        });
    }

    public static void init() {
        Main.game.add(playButton);
        Main.game.add(settingsButton);
        Main.game.add(aboutButton);
        Main.game.add(exitButton);
        playButton.setVisible(false);
        settingsButton.setVisible(false);
        aboutButton.setVisible(false);
        exitButton.setVisible(false);
    }

    @Override
    public void start() {
        playButton.setVisible(true);
        settingsButton.setVisible(true);
        aboutButton.setVisible(true);
        exitButton.setVisible(true);
        menuLoop.loop(Clip.LOOP_CONTINUOUSLY);

        // Background
        BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = background.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 1920, 1080);
        g2d.dispose();
        Main.background.setBackground(background);
    }

    @Override
    public void end() {
        playButton.setVisible(false);
        settingsButton.setVisible(false);
        aboutButton.setVisible(false);
        exitButton.setVisible(false);
        menuLoop.stop();
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.drawImage(logo, 50, 1080 / 2 - 450, 400, 400, null);
        g2d.drawImage(logo, 50, 1080 / 2 + 50, 400, 400, null);
        g2d.drawImage(logo, 1920 - 450, 1080 / 2 - 450, 400, 400, null);
        g2d.drawImage(logo, 1920 - 450, 1080 / 2 + 50, 400, 400, null);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
