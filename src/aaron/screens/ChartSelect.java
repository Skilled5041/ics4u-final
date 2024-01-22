// Aaron Ye
// 2024-01-21

package aaron.screens;

import aaron.Game;
import aaron.Main;
import aaron.charts.Chart;
import aaron.graphics.ChartSelector;
import aaron.graphics.CustomButton;
import aaron.graphics.Screen;

import javax.sound.sampled.Clip;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartSelect implements Screen {
    private static final Map<String, List<Chart>> charts = new HashMap<>();
    private static int chartCount = 0;
    private Clip backgroundMusic;
    private String currentSongName = "";
    private double songPreviewTime = 0;
    private static final CustomButton playButton = new CustomButton(500, 200, "Play");
    private static final CustomButton backButton = new CustomButton(500, 200, "Back");

    /**
     * Initialises the charts
     */
    public static void init() {
        // Get directories inside the charts directory
        File chartsDir = new File("charts");
        String[] subDirs = chartsDir.list((dir, name) -> new File(dir, name).isDirectory());

        if (subDirs == null) {
            return;
        }

        // Loads all the charts
        for (String s : subDirs) {
            charts.put(s.toLowerCase(), new ArrayList<>());
            File dir = new File("charts/" + s);
            String[] maps = dir.list((dir1, name) -> name.toLowerCase().endsWith(".qua"));

            if (maps == null) {
                continue;
            }

            for (String map : maps) {
                charts.get(s.toLowerCase()).add(Chart.parse("charts/" + s.toLowerCase(), map));
                chartCount++;
            }
        }

        // Create chart selectors
        for (String s : charts.keySet()) {
            for (Chart chart : charts.get(s)) {
                ChartSelector chartSelector = new ChartSelector(chart);
                Main.game.add(chartSelector);
                chartSelectors.add(chartSelector);
                chartSelector.setVisible(false);
            }
        }

        // Sort them
        Collections.sort(chartSelectors);

        // Select the first chart
        chartSelectors.get(chartIndex).setSelected(true);
        Main.game.add(playButton);
        playButton.setLocation(200, 350);
        playButton.onClick(e -> {
            Game4Key.setChart(chartSelectors.get(chartIndex).getChart());
            Main.game.switchScreen(Game.Screens.GAME_4KEY);
        });
        playButton.setVisible(false);
        Main.game.add(backButton);
        backButton.setLocation(200, 650);
        backButton.onClick(e -> Main.game.switchScreen(Game.Screens.MAIN_MENU));
        backButton.setVisible(false);
    }

    private static final ArrayList<ChartSelector> chartSelectors = new ArrayList<>();
    // The chart that is currently selected
    private static int chartIndex = 0;

    /**
     * Shows the chart list based on the current chart index
     */
    public static void showChartSelectors() {
        // Hide all chart selectors
        for (ChartSelector chartSelector : chartSelectors) {
            chartSelector.setVisible(false);
        }

        // SHow up to next 5 chart selectors
        int index = 0;
        for (int i = chartIndex; i < Math.min(chartIndex + 5, chartSelectors.size()); i++) {
            chartSelectors.get(i).setVisible(true);
            chartSelectors.get(i).setLocation(1920 - ChartSelector.WIDTH - 20, 60 + 200 * (index));
            index++;
        }
    }

    @Override
    public void start() {
        // Sets the background image
        BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = background.createGraphics();
        g2d.drawImage(chartSelectors.get(chartIndex).getBackgroundImage(), 0, 0, 1920, 1080, null);
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, 1920, 1080);
        g2d.dispose();
        Main.background.setBackground(background);

        // Play background music
        backgroundMusic = chartSelectors.get(chartIndex).getAudio();

        // In ms
        // Play song starting at song preview time
        double spt = chartSelectors.get(chartIndex).getSongPreviewTime();
        backgroundMusic.setMicrosecondPosition((long) (spt * 1000));
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        currentSongName = chartSelectors.get(chartIndex).getSongName();
        songPreviewTime = chartSelectors.get(chartIndex).getSongPreviewTime();
        playButton.setVisible(true);
        backButton.setVisible(true);
        showChartSelectors();
    }

    @Override
    public void end() {
        // Make all chart selectors invisible
        for (ChartSelector chartSelector : chartSelectors) {
            chartSelector.setVisible(false);
        }
        playButton.setVisible(false);
        backButton.setVisible(false);
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        Main.background.clearBackground();
    }

    @Override
    public void render(Graphics2D g2d) {
        // Scroll bar
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.fillRect(1920 - 20, 0, 20, 1080);
        // Scroll bar handle
        g2d.setColor(new Color(5, 134, 228, 255));
        int handleSize = 1080 / Math.max(1, (chartCount)) + 1;
        int handlePos = chartIndex * handleSize;
        g2d.fillRect(1920 - 20, handlePos, 20, handleSize);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Play if space is pressed enter the game
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Game4Key.setChart(chartSelectors.get(chartIndex).getChart());
            Main.game.switchScreen(Game.Screens.GAME_4KEY);
            return;
        }

        // Scroll through the charts with arrows
        chartSelectors.get(chartIndex).setSelected(false);
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (chartIndex > 0) {
                chartIndex--;
                showChartSelectors();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (chartIndex < chartSelectors.size() - 1) {
                chartIndex++;
                showChartSelectors();
            }
        }
        refresh();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Scroll through maps using scroll wheel
        chartSelectors.get(chartIndex).setSelected(false);
        if (e.getWheelRotation() < 0) {
            if (chartIndex > 0) {
                chartIndex--;
                showChartSelectors();
            }
        } else if (e.getWheelRotation() > 0) {
            if (chartIndex < chartSelectors.size() - 1) {
                chartIndex++;
                showChartSelectors();
            }
        }

        refresh();
    }

    private void refresh() {
        chartSelectors.get(chartIndex).setSelected(true);

        // Change background image
        BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = background.createGraphics();
        g2d.drawImage(chartSelectors.get(chartIndex).getBackgroundImage(), 0, 0, 1920, 1080, null);
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, 1920, 1080);
        g2d.dispose();
        Main.background.setBackground(background);

        // Change song if a new song is selected or different song preview time
        if (!chartSelectors.get(chartIndex).getSongName().equals(currentSongName) || songPreviewTime != chartSelectors.get(chartIndex).getSongPreviewTime()) {
            songPreviewTime = chartSelectors.get(chartIndex).getSongPreviewTime();
            currentSongName = chartSelectors.get(chartIndex).getSongName();
            backgroundMusic.stop();
            backgroundMusic = chartSelectors.get(chartIndex).getAudio();
            double songPreviewTime = chartSelectors.get(chartIndex).getSongPreviewTime();
            backgroundMusic.setMicrosecondPosition((long) (songPreviewTime * 1000));
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
