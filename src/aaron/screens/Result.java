// Aaron Ye
// 2024-01-21

package aaron.screens;

import aaron.Game;
import aaron.Main;
import aaron.charts.Chart;
import aaron.graphics.CustomButton;
import aaron.graphics.Screen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class Result implements Screen {
    private static Chart chart;
    private static final CustomButton playAgainButton = new CustomButton(500, 200, "Play Again");
    private static final CustomButton backButton = new CustomButton(500, 200, "Back");
    // Initialize buttons
    static {
        playAgainButton.setLocation(1920 / 2 - 500, 1080 / 2 + 250);
        backButton.setLocation(1920 / 2 + 50, 1080 / 2 + 250);
        playAgainButton.onClick(e -> {
            Main.game.switchScreen(Game.Screens.GAME_4KEY);
        });
        backButton.onClick(e -> {
            Main.game.switchScreen(Game.Screens.CHART_SELECT);
        });
    }

    /**
     * Avoids null error
     */
    public static void init() {
        Main.game.add(playAgainButton);
        Main.game.add(backButton);
        playAgainButton.setVisible(false);
        backButton.setVisible(false);
    }

    public static void setChart(Chart c) {
        chart = c;
    }

    private static int missCount = 0;
    private static int okayCount = 0;
    private static int goodCount = 0;
    private static int greatCount = 0;
    private static int perfectCount = 0;
    private static int marvelousCount = 0;

    public static void setMissCount(int mc) {
        missCount = mc;
    }

    public static void setOkayCount(int oc) {
        okayCount = oc;
    }

    public static void setGoodCount(int gc) {
        goodCount = gc;
    }

    public static void setGreatCount(int gc) {
        greatCount = gc;
    }

    public static void setPerfectCount(int pc) {
        perfectCount = pc;
    }

    public static void setMarvelousCount(int mc) {
        marvelousCount = mc;
    }

    private static int maxCombo = 0;
    private static int score = 0;
    private static double accuracy = 0;

    public static void setMaxCombo(int mc) {
        maxCombo = mc;
    }

    public static void setScore(int s) {
        score = s;
    }

    public static void setAccuracy(double a) {
        accuracy = a;
    }

    @Override
    public void start() {
        // Background
        BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = background.createGraphics();
        g2d.drawImage(chart.getBackground(), 0, 0, null);
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 0, 1920, 1080);
        g2d.setColor(new Color(0, 0, 0, 255));
        aaron.graphics.Utils.drawCenteredFilledRectangle(g2d, 1920 / 2, 1080 / 2 - 100, 1200, 600);
        // Outline
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(255, 255, 255, 255));
        aaron.graphics.Utils.drawCenteredRectangle(g2d, 1920 / 2, 1080 / 2 - 100, 1200, 600);
        g2d.dispose();
        Main.background.setBackground(background);

        playAgainButton.setVisible(true);
        backButton.setVisible(true);
    }

    @Override
    public void end() {
        playAgainButton.setVisible(false);
        backButton.setVisible(false);
    }

    @Override
    public void render(Graphics2D g2d) {
        // Hit accuracies
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.setFont(Game.fontSmall);
        g2d.drawString("Miss: " + missCount, 1920 / 2 - 500, 1080 / 2 - 300);
        g2d.drawString("Okay: " + okayCount, 1920 / 2 - 500, 1080 / 2 - 250);
        g2d.drawString("Good: " + goodCount, 1920 / 2 - 500, 1080 / 2 - 200);
        g2d.drawString("Great: " + greatCount, 1920 / 2 - 500, 1080 / 2 - 150);
        g2d.drawString("Perfect: " + perfectCount, 1920 / 2 - 500, 1080 / 2 - 100);
        g2d.drawString("Marvelous: " + marvelousCount, 1920 / 2 - 500, 1080 / 2 - 50);
        // Combo
        g2d.setFont(Game.fontMedium);
        g2d.drawString("Max Combo: " + maxCombo, 1920 / 2 - 500, 1080 / 2 + 100);
        // Accuracy
        g2d.drawString("Accuracy: " + String.format("%.2f%%", accuracy * 100), 1920 / 2 + 50, 1080 / 2);
        // Score
        g2d.drawString("Score: " + score, 1920 / 2 + 50, 1080 / 2 + 100);

        // Show grade image
        if (accuracy == 1) {
            g2d.drawImage(Game4Key.rankingImages.get("SS"), 1920 / 2 + 200, 1080 / 2 - 250, null);
        } else if (accuracy >= 0.95) {
            g2d.drawImage(Game4Key.rankingImages.get("S"), 1920 / 2 + 200, 1080 / 2 - 250, null);
        } else if (accuracy >= 0.90) {
            g2d.drawImage(Game4Key.rankingImages.get("A"), 1920 / 2 + 200, 1080 / 2 - 250, null);
        } else if (accuracy >= 0.80) {
            g2d.drawImage(Game4Key.rankingImages.get("B"), 1920 / 2 + 200, 1080 / 2 - 250, null);
        } else if (accuracy >= 0.70) {
            g2d.drawImage(Game4Key.rankingImages.get("C"), 1920 / 2 + 200, 1080 / 2 - 250, null);
        } else {
            g2d.drawImage(Game4Key.rankingImages.get("D"), 1920 / 2 + 200, 1080 / 2 - 250, null);
        }
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
