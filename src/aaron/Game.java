package aaron;

import aaron.graphics.Screen;
import aaron.screens.ChartSelect;
import aaron.screens.Game4Key;
import aaron.screens.MainMenu;
import aaron.screens.Result;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Game extends JPanel implements KeyListener, MouseWheelListener {
    public static Font fontSmall = new Font("Aller", Font.PLAIN, 24);
    public static Font fontSmaller = fontSmall.deriveFont(20f);
    public static Font fontMedium = fontSmall.deriveFont(64f);

    public Game() {
        setOpaque(false);
        addKeyListener(this);
        addMouseWheelListener(this);
        setFocusable(true);
        int fpsCap = 300;
        Timer frameRenderer = new Timer((int) Math.ceil(1000.0 / fpsCap), e -> repaint());
        frameRenderer.start();
    }

    public void start() {
        switchScreen(currentScreen);
    }

    // Size of the window
    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;
    Toolkit toolkit = Toolkit.getDefaultToolkit();

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        screens.get(currentScreen).mouseWheelMoved(e);
    }

    public enum Screens {
        GAME_4KEY,
        CHART_SELECT,
        RESULT,
        MAIN_MENU,
        SETTINGS,
        ABOUT
    }

    Screens currentScreen = Screens.MAIN_MENU;

    Map<Screens, Screen> screens = new HashMap<>() {{
        put(Screens.GAME_4KEY, new Game4Key());
        put(Screens.CHART_SELECT, new ChartSelect());
        put(Screens.RESULT, new Result());
        put(Screens.MAIN_MENU, new MainMenu());
        put(Screens.SETTINGS, new aaron.screens.Settings());
        put(Screens.ABOUT, new aaron.screens.About());
    }};

    /**
     * Switches the current game screen
     *
     * @param screen New screen
     */
    public void switchScreen(Screens screen) {
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
        g2dBuffer.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2dBuffer.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2dBuffer.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2dBuffer.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2dBuffer.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
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
