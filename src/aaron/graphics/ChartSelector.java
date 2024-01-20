package aaron.graphics;

import aaron.Game;
import aaron.charts.Chart;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ChartSelector extends JComponent {
    public final static int WIDTH = 1100;
    public final static int HEIGHT = 160;
    private final Chart chart;

    public ChartSelector(Chart chart) {
        setSize(WIDTH, HEIGHT);
        // Allow scrolling
        setAutoscrolls(true);
        this.chart = chart;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(Game.fontSmall);

        g2d.setColor(new Color(52, 52, 52, 255));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Outline
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(5, 134, 228, 255));
        g2d.drawRect(0, 0, WIDTH + 4, HEIGHT);

        // Chart name
        g2d.setColor(Color.WHITE);
        String name = String.format("%s - %s", chart.getSongArtist(), chart.getSongTitle());
        g2d.drawString(name, 30, 50);

        // Difficulty
        g2d.setFont(Game.fontSmaller);
        g2d.drawString(chart.getDifficultyName(), 30, 85);

        // Chart creator
        g2d.drawString("By: " + chart.getCreator(), 30, 120);

        // Crop background
        BufferedImage background = chart.getBackground();
        BufferedImage cropped = background.getSubimage(background.getWidth() / 2, background.getHeight() / 2, background.getWidth() / 2, 120);
        g2d.drawImage(cropped, 350, 20, 1920, 120, null);
    }
}