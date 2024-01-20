package aaron.screens;

import aaron.Main;
import aaron.charts.Chart;
import aaron.graphics.ChartSelector;
import aaron.graphics.Screen;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartSelect implements Screen {
    private static final Map<String, List<Chart>> charts = new HashMap<>();
    public static boolean loaded = false;

    public static void init() {
        // Get directories inside the charts directory
        File chartsDir = new File("charts");
        String[] subDirs = chartsDir.list((dir, name) -> new File(dir, name).isDirectory());

        if (subDirs == null) {
            return;
        }

        for (String s : subDirs) {
            charts.put(s.toLowerCase(), new ArrayList<>());
            File dir = new File("charts/" + s);
            String[] maps = dir.list((dir1, name) -> name.toLowerCase().endsWith(".qua"));

            if (maps == null) {
                continue;
            }

            for (String map : maps) {
                charts.get(s.toLowerCase()).add(Chart.parse("charts/" + s.toLowerCase(), map));
            }
        }

        // Create a new ChartSelector for each chart
        int index = 0;
        for (String s : charts.keySet()) {
            for (Chart chart : charts.get(s)) {
                ChartSelector chartSelector = new ChartSelector(chart);
                Main.game.add(chartSelector);
                chartSelector.setBounds(1920 - ChartSelector.WIDTH, 30 + index * (ChartSelector.HEIGHT + 12), ChartSelector.WIDTH, ChartSelector.HEIGHT);
                chartSelector.setVisible(true);
                index++;
            }
        }
    }


    @Override
    public void start() {
        if (!loaded) {
            init();
            loaded = true;
        }
    }

    @Override
    public void end() {

    }

    @Override
    public void render(Graphics2D g2d) {
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
}
