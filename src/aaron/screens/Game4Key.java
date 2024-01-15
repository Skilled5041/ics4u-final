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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game4Key implements Screen {
    public Game4Key() throws IOException {
    }

    private final ArrayList<Note>[] currentGameNotes = (ArrayList<Note>[]) new ArrayList[4];

    @Override
    public void start() throws LineUnavailableException, IOException {
        chart = Chart.parse("charts/2", "18167.qua");
        assert chart != null;
        song = chart.getAudio();

        // Clone the notes
        for (int i = 0; i < 4; i++) {
            currentGameNotes[i] = new ArrayList<>(chart.getNotes()[i]);
        }

        Timer startTimer = new Timer(2000, e -> {
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
    private final int scrollSpeed = 10;
    private int[] minIndex = new int[4];
    private final boolean[] laneHeld = new boolean[4];
    private final BufferedImage hitGradient = ImageIO.read(new File("resources/skin/hit_gradient.png"));
    private double accuracy = 1;
    private int accuracySampleCount = 0;

    private int missCount = 0;
    private int okayCount = 0;
    private int goodCount = 0;
    private int greatCount = 0;
    private int perfectCount = 0;
    private int marvelousCount = 0;

    @Override
    public void render(Graphics2D g2d) {
        // Main play area
        g2d.setStroke(new BasicStroke(2));
        aaron.graphics.Utils.drawCenteredRectangle(g2d, 960, 540, 600, 1084);

        // Note receptor
        g2d.setColor(new Color(249, 180, 222, 255));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(960 - 300 + 2, 1080 - 100, 960 + 300 - 2, 1080 - 100);

        // Draw the columns
        // TODO: maybe have setting for this
//        g2d.setColor(new Color(255, 255, 255, 20));
//        g2d.drawLine(960 - 1, 0, 960 - 1, 1080);
//        g2d.drawLine(960 - 150 - 1, 0, 960 - 150 - 1, 1080);
//        g2d.drawLine(960 + 150 - 1, 0, 960 + 150 - 1, 1080);
        long currentSongTime = (long) (song.getLongFramePosition() * 1000 / song.getFormat().getFrameRate());
        for (int i = 0; i < currentGameNotes.length; i++) {
            ArrayList<Note> notesList = currentGameNotes[i];
            for (int j = minIndex[i]; j < notesList.size(); j++) {
                Note note = notesList.get(j);

                // If a normal note has gone offscreen, and it has not been hit, count it as a miss
                if (note.getStartTime() + aaron.rhythm.Utils.getMaxOkayTime() < currentSongTime && note.getEndTime() == -1 && !note.isHit()) {
                    minIndex[i] = j + 1;
                    accuracySampleCount++;
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                    missCount++;
                    note.setHit(true);
                    continue;
                // If the end of a long note has gone offscreen, and it has not been released TODO: implement
                } else if (note.getEndTime() != -1 && note.getEndTime() + aaron.rhythm.Utils.getMaxOkayTime() < currentSongTime) {
                    minIndex[i] = j + 1;
//                    accuracySampleCount++;
//                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                    continue;
                }
//                System.out.println(missCount);
                // If the start of a long note has gone offscreen,
                // and it has not been hit, count it as a miss
                if (note.getEndTime() != -1 && note.getStartTime() + aaron.rhythm.Utils.getMaxOkayTime() < currentSongTime && !note.isHit()) {
                    accuracySampleCount++;
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                    missCount++;
                    note.setHit(true);
                }
                // Break if notes are offscreen (they have not appeared yet)
                if (note.getStartTime() > currentSongTime + aaron.rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed)) {
                    break;
                }

                // Normal note
                if (note.getEndTime() == -1) {
                    drawNote(g2d, note.getLane(), note.getStartTime(), currentSongTime);
                } else {
                    drawLongNote(g2d, note.getLane(), note.getStartTime(), note.getEndTime(), currentSongTime);
                }
            }
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

        // TODO: make the y value more accurate
        g2d.fillRect(960 - 450 + (lane * 150), convertNoteTimeToY(time, currentSongTime, scrollSpeed) - 50, 150, 50);
    }

    public void drawLongNote(Graphics2D g2d, int lane, int startTime, int endTime, long currentSongTime) {
        g2d.setStroke(new BasicStroke(1));
        if (lane == 1 || lane == 4) {
            g2d.setColor(new Color(253, 253, 253, 255));
        } else {
            g2d.setColor(new Color(80, 195, 247, 255));
        }

        // TODO: make the y value more accurate
        g2d.fillRect(960 - 450 + (lane * 150),
                getLongNoteEndY(endTime, currentSongTime, scrollSpeed),
                150,
                getLongNoteHeight(startTime, endTime, scrollSpeed));
    }

    private static int convertNoteTimeToY(int time, long currentSongTime, int scrollSpeed) {
        int scrollSpeedTimeWidth = aaron.rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed);
        double decimal = (double) (time - currentSongTime) / scrollSpeedTimeWidth;
        return (int) (1080 - decimal * 1080);
    }

    private static int getLongNoteEndY(int endTime, long currentSongTime, int scrollSpeed) {
        int scrollSpeedTimeWidth = aaron.rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed);
        double decimal = (double) (endTime - currentSongTime) / scrollSpeedTimeWidth;
        return (int) (1080 - decimal * 1080);
    }

    private static int getLongNoteHeight(int startTime, int endTime, int scrollSpeed) {
        int scrollSpeedTimeWidth = aaron.rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed);
        double decimal = (double) (endTime - startTime) / scrollSpeedTimeWidth;
        return (int) (decimal * 1080);
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
            // Return if already held
            if (laneHeld[lane - 1]) {
                return;

            }

            laneHeld[lane - 1] = true;

            Note nextNote = currentGameNotes[lane - 1].get(minIndex[lane - 1]);
            // Ignore note since too far away
            int timingInaccuracy = (int) Math.abs(nextNote.getStartTime() - Utils.getCurrentSongTime(song));
            if (timingInaccuracy > 164) {
                return;
            }

            accuracySampleCount++;

            // Normal note
            if (nextNote.getEndTime() == -1) {
                // Long notes should not disappear when first hit
                minIndex[lane - 1]++;
            }

            if (timingInaccuracy > aaron.rhythm.Utils.getMaxGoodTime()) {
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getOkayAccuracy(), accuracySampleCount);
                okayCount++;
            } else if (timingInaccuracy > aaron.rhythm.Utils.getMaxGreatTime()) {
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getGoodAccuracy(), accuracySampleCount);
                goodCount++;
            } else if (timingInaccuracy > aaron.rhythm.Utils.getMaxPerfectTime()) {
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getGreatAccuracy(), accuracySampleCount);
                greatCount++;
            } else if (timingInaccuracy > aaron.rhythm.Utils.getMaxMarvelousTime()) {
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getPerfectAccuracy(), accuracySampleCount);
                perfectCount++;
            } else {
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMarvelousAccuracy(), accuracySampleCount);
                marvelousCount++;
            }

            nextNote.setHit(true);
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
