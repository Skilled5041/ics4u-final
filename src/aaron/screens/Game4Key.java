package aaron.screens;

import aaron.charts.Chart;
import aaron.charts.Note;
import aaron.graphics.Screen;
import aaron.rhythm.Utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Game4Key implements Screen {
    public Game4Key() throws IOException {
    }

    @Override
    public void start() throws LineUnavailableException, IOException {
        chart = Chart.parse("charts/3", "23102.qua");
        song = chart.getAudio();

        Timer startTimer = new Timer(5000, e -> {
            song.start();
        });
        startTimer.setRepeats(false);
        startTimer.start();
    }

    @Override
    public void end() {

    }

    private Chart chart;
    private Clip song;
    private final int scrollSpeed = 15;
    private int minIndex = 0;
    private boolean[] laneHeld = new boolean[4];
    private BufferedImage hitGradient = ImageIO.read(new File("resources/skin/hit_gradient.png"));
    private double accuracy = 1;
    private int accuracySampleCount = 0;

    @Override
    public void render(Graphics2D g2d) {
        // Main play area
        g2d.setStroke(new BasicStroke(2));
        aaron.graphics.Utils.drawCenteredRectangle(g2d, 960, 540, 600, 1082);
        g2d.setColor(new Color(249, 180, 222, 255));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(960 - 300, 1080 - 100, 960 + 300, 1080 - 100);

        // Draw the columns
        // TODO: maybe have setting for this
//        g2d.setColor(new Color(255, 255, 255, 20));
//        g2d.drawLine(960 - 1, 0, 960 - 1, 1080);
//        g2d.drawLine(960 - 150 - 1, 0, 960 - 150 - 1, 1080);
//        g2d.drawLine(960 + 150 - 1, 0, 960 + 150 - 1, 1080);

        long currentSongTime = (long) (song.getLongFramePosition() * 1000 / song.getFormat().getFrameRate());
        for (int i = minIndex; i < chart.getNotes().size(); i++) {
            Note note = chart.getNotes().get(i);
            if (note.startTime() + aaron.rhythm.Utils.getMaxOkayTime() < currentSongTime) {
                minIndex = i + 1;
                accuracySampleCount++;
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                continue;
            }
            if (note.endTime() > currentSongTime + aaron.rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed)) {
                break;
            }
            drawNote(g2d, note.Lane(), note.startTime(), currentSongTime);
        }

        for (int i = 0; i < 4; i++) {
            if (laneHeld[i]) {
                g2d.drawImage(hitGradient, 960 - 300 + (i * 150), 600, 150, 1080, null);
            }
        }

        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.drawString(String.format("Accuracy: %.2f%%", accuracy * 100), 1920 - 170, 20);
    }

    private void drawNote(Graphics2D g2d, int lane, int time, long currentSongTime) {
        g2d.setStroke(new BasicStroke(1));
        if (lane == 1 || lane == 4) {
            g2d.setColor(new Color(253, 253, 253, 255));
        } else {
            g2d.setColor(new Color(80, 195, 247, 255));
        }

        g2d.fillRect(960 - 450 + (lane * 150), convertNoteTimeToY(time, currentSongTime, scrollSpeed) - 50, 150, 50);
    }

    private static int convertNoteTimeToY(int time, long currentSongTime, int scrollSpeed) {
        int scrollSpeedTimeWidth = aaron.rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed);
        double decimal = (double) (time - currentSongTime) / scrollSpeedTimeWidth;
        return (int) (1080 - decimal * 1080);
    }

    public void drawLongNote(int lane) {

    }

    Map<Integer, Integer> keycodeToLane = new HashMap<>() {{
        put(KeyEvent.VK_A, 1);
        put(KeyEvent.VK_S, 2);
        put(KeyEvent.VK_K, 3);
        put(KeyEvent.VK_L, 4);
    }};

    @Override

    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int lane = keycodeToLane.getOrDefault(e.getKeyCode(), -1);
        if (lane >= 1 && lane <= 4) {
            laneHeld[lane - 1] = true;

            Note nextNote = chart.getNotes().get(minIndex);
            // Ignore note since too far away
            if (Math.abs(nextNote.startTime() - aaron.rhythm.Utils.getCurrentSongTime(song)) > 164) {
                return;
            }

            // Normal note
            minIndex++;
            accuracySampleCount++;
            if (nextNote.endTime() == -1) {
                if (Math.abs(nextNote.startTime() - aaron.rhythm.Utils.getCurrentSongTime(song)) > aaron.rhythm.Utils.getMaxOkayTime()) {
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                } else if (Math.abs(nextNote.startTime() - aaron.rhythm.Utils.getCurrentSongTime(song)) > aaron.rhythm.Utils.getMaxGoodTime()) {
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getOkayAccuracy(), accuracySampleCount);
                } else if (Math.abs(nextNote.startTime() - aaron.rhythm.Utils.getCurrentSongTime(song)) > aaron.rhythm.Utils.getMaxGreatTime()) {
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getGoodAccuracy(), accuracySampleCount);
                } else if (Math.abs(nextNote.startTime() - aaron.rhythm.Utils.getCurrentSongTime(song)) > aaron.rhythm.Utils.getMaxPerfectTime()) {
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getGreatAccuracy(), accuracySampleCount);
                } else if (Math.abs(nextNote.startTime() - aaron.rhythm.Utils.getCurrentSongTime(song)) > aaron.rhythm.Utils.getMaxMarvelousTime()) {
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getPerfectAccuracy(), accuracySampleCount);
                } else {
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMarvelousAccuracy(), accuracySampleCount);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int lane = keycodeToLane.getOrDefault(e.getKeyCode(), -1);
        if (lane >= 1 && lane <= 4) {
            laneHeld[lane - 1] = false;
        }
    }
}
