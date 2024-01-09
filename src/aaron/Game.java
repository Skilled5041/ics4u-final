package aaron;

import aaron.graphics.Screen;
import aaron.screens.Game4Key;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game extends JPanel implements KeyListener {
    public static Font fontSmall = new Font("Aller", Font.PLAIN, 18);

    public Game() throws LineUnavailableException, IOException {
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        int fpsCap = 120;
        Timer frameRenderer = new Timer((int) Math.ceil(1000.0 / fpsCap),  e -> {
            toolkit.sync();
            repaint();
        });
        frameRenderer.start();
        switchScreen(currentScreen);
    }

    // Size of the window
    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;
    Toolkit toolkit = Toolkit.getDefaultToolkit();

    public enum Screens {
        GAME_4KEY
    }

    Screens currentScreen = Screens.GAME_4KEY;

    Map<Screens, Screen> screens = new HashMap<>() {{
        put(Screens.GAME_4KEY, new Game4Key());
    }};

    /**
     * Switches the current game screen
     *
     * @param screen New screen
     */
    public void switchScreen(Screens screen) throws LineUnavailableException, IOException {
        screens.get(currentScreen).end();
        currentScreen = screen;
        screens.get(currentScreen).start();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) (g);

        // Buffer
        BufferedImage buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dBuffer = buffer.createGraphics();
        g2dBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2dBuffer.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2dBuffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2dBuffer.setFont(Game.fontSmall);

        // Render the screen
        screens.get(currentScreen).render(g2dBuffer);
        g2d.drawImage(buffer, null, 0, 0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        screens.get(currentScreen).keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        screens.get(currentScreen).keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        screens.get(currentScreen).keyReleased(e);
    }

    public void onExit() {
        screens.get(currentScreen).end();
    }
}
