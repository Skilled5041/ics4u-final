package aaron.screens;

import aaron.charts.Chart;
import aaron.charts.Note;
import aaron.graphics.EasingFunctions;
import aaron.graphics.Screen;
import aaron.rhythm.Utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
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
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static aaron.Game.fontMedium;
import static aaron.Utils.Clamp;
import static aaron.graphics.Utils.drawCenteredImage;

public class Game4Key implements Screen {
    public Game4Key() {
    }

    private final ArrayList<Note>[] currentGameNotes = (ArrayList<Note>[]) new ArrayList[4];
    private final Map<String, BufferedImage> rankingImages = new HashMap<>() {{
        try {
            put("SS", ImageIO.read(new File("resources/skin/rank_ss.png")));
            put("S", ImageIO.read(new File("resources/skin/rank_s.png")));
            put("A", ImageIO.read(new File("resources/skin/rank_a.png")));
            put("B", ImageIO.read(new File("resources/skin/rank_b.png")));
            put("C", ImageIO.read(new File("resources/skin/rank_c.png")));
            put("D", ImageIO.read(new File("resources/skin/rank_d.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }};

    private final Map<String, BufferedImage> hitScoreImages = new HashMap<>() {{
        try {
            put("marvelous", ImageIO.read(new File("resources/skin/marvelous.png")));
            put("perfect", ImageIO.read(new File("resources/skin/perfect.png")));
            put("great", ImageIO.read(new File("resources/skin/great.png")));
            put("good", ImageIO.read(new File("resources/skin/good.png")));
            put("okay", ImageIO.read(new File("resources/skin/okay.png")));
            put("miss", ImageIO.read(new File("resources/skin/miss.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }};


    private BufferedImage hitMetre;
    private BufferedImage arrow;

    {
        try {
            hitMetre = ImageIO.read(new File("resources/skin/hit_metre.png"));
            arrow = ImageIO.read(new File("resources/skin/arrow.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        chart = Chart.parse("charts/3", "23107.qua");
        assert chart != null;
        song = chart.getAudio();

        // Clone the notes
        for (int i = 0; i < 4; i++) {
            currentGameNotes[i] = new ArrayList<>(chart.getNotes()[i]);
        }

        Timer startTimer = new Timer(3000, e -> {
            song.start();
        });
        startTimer.setRepeats(false);
        startTimer.start();
    }

    @Override
    public void end() {

    }

    private String currentHitScore = "";
    int hitScoreTimeDuration = 300;
    Timer clearHitScoreTimer = new Timer(hitScoreTimeDuration, e -> currentHitScore = "");
    int hitScoreTime = -1;

    {
        clearHitScoreTimer.setRepeats(false);
    }

    private static Map<String, Integer> judgementToScore = new HashMap<>() {{
        put("marvelous", 1000);
        put("perfect", 1000);
        put("great", 800);
        put("good", 600);
        put("okay", 400);
        put("miss", 0);
    }};

    int currentCombo = 0;
    int maxCombo = 0;
    int score = 0;

    private void setHitScore(String hitScore) {
        currentCombo++;
        maxCombo = Math.max(maxCombo, currentCombo);
        score += (int) (judgementToScore.get(hitScore) * (1 + (Math.max(1, Math.log10(currentCombo)) - 1) * 0.1F));
        if (hitScore.equals("miss")) {
            currentCombo = 0;
        }

        if (hitScore.equals("marvelous")) {
            return;
        }

        hitScoreTime = (int) Utils.getCurrentSongTime(song);
        currentHitScore = hitScore;
        if (clearHitScoreTimer.isRunning()) {
            clearHitScoreTimer.restart();
        } else {
            clearHitScoreTimer.start();
        }
    }

    private Chart chart;
    private Clip song;
    private final int scrollSpeed = 10;
    private int[] minIndex = new int[4];
    private final boolean[] laneHeld = new boolean[4];
    private BufferedImage hitGradient;

    {
        try {
            hitGradient = ImageIO.read(new File("resources/skin/hit_gradient.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double accuracy = 1;
    private int accuracySampleCount = 0;

    // In ms
    private Queue<Integer> hitOffsets = new LinkedList<>();
    private int averageHitOffset = 0;

    private int missCount = 0;
    private int okayCount = 0;
    private int goodCount = 0;
    private int greatCount = 0;
    private int perfectCount = 0;
    private int marvelousCount = 0;

    @Override
    public void render(Graphics2D g2d) {
        // Draw the background
        g2d.drawImage(chart.getBackground(), 0, 0, 1920, 1080, null);

        // Dim the background
        g2d.setColor(new Color(0, 0, 0, 220));
        g2d.fillRect(0, 0, 1920, 1080);

        // Main play area
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(0, 0, 0));
        aaron.graphics.Utils.drawCenteredFilledRectangle(g2d, 960, 540, 600, 1084);

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
                    setHitScore("miss");
                    missCount++;
                    note.setHit(true);
                    continue;
                    // If the end of a long note has gone offscreen, and it has not been released
                } else if (note.getEndTime() != -1 && note.getEndTime() + aaron.rhythm.Utils.getMaxOkayTime() < currentSongTime && !note.isReleased()) {
                    minIndex[i] = j + 1;
                    accuracySampleCount++;
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                    continue;
                }
                // If the start of a long note has gone offscreen,
                // and it has not been hit, count it as a miss
                if (note.getEndTime() != -1 && note.getStartTime() + aaron.rhythm.Utils.getMaxOkayTime() < currentSongTime && !note.isHit()) {
                    accuracySampleCount++;
                    accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                    setHitScore("miss");
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
                } else if (!laneHeld[i]) {
                    drawLongNote(g2d, note.getLane(), note.getStartTime(), note.getEndTime(), currentSongTime);
                } else {
                    drawLongNoteHeld(g2d, note.getLane(), note.getStartTime(), note.getEndTime(), currentSongTime);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (laneHeld[i]) {
                g2d.drawImage(hitGradient, 960 - 300 + (i * 150), 600, 150, 1080, null);
            }
        }

        // Draw accuracy
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.drawString(String.format("%6.2f%%", accuracy * 100), 1920 - 110, 30);

        // Draw score
        g2d.drawString(String.format("%08d", score), 1920 - 125, 70);

        // Grade
        if (accuracy == 1) {
            g2d.drawImage(rankingImages.get("SS"), 1920 - 140, 10, 24, 30, null);
        } else if (accuracy >= 0.95) {
            g2d.drawImage(rankingImages.get("S"), 1920 - 140, 10, 24, 30, null);
        } else if (accuracy >= 0.9) {
            g2d.drawImage(rankingImages.get("A"), 1920 - 140, 10, 24, 30, null);
        } else if (accuracy >= 0.8) {
            g2d.drawImage(rankingImages.get("B"), 1920 - 140, 10, 24, 30, null);
        } else if (accuracy >= 0.7) {
            g2d.drawImage(rankingImages.get("C"), 1920 - 140, 10, 24, 30, null);
        } else {
            g2d.drawImage(rankingImages.get("D"), 1920 - 140, 10, 24, 30, null);
        }

        // Draw current hit score
        if (!currentHitScore.isEmpty()) {
            double scale = EasingFunctions.easeInOutExpo((double) (hitScoreTime + 100 - currentSongTime) / 100);
            drawCenteredImage(g2d, 1920 / 2, 1080 / 2 - 200, hitScoreImages.get(currentHitScore), Clamp(1 - scale, 0, 1) / 4 + 0.25);
        }

        // Draw combo text
        g2d.setColor(new Color(255, 255, 255, 255));
        g2d.setFont(fontMedium);
        g2d.drawString(currentCombo + "x", 10, 1080 - 25);

        // Hit timing

        // Hit meter
        drawCenteredImage(g2d, 1920 / 2, 500, hitMetre, 1);
        // Arrow
        drawCenteredImage(g2d, 1920 / 2 + averageHitOffset, 480, arrow, 0.29);
        // Draw hit lines
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.setStroke(new BasicStroke(1));

        for (int i : hitOffsets) {
            if (Math.abs(i) > 127) {
                continue;
            }
            g2d.drawLine(1920 / 2 + i, 480, 1920 / 2 + i, 520);
        }
    }

    private void drawNote(Graphics2D g2d, int lane, int time, long currentSongTime) {
        g2d.setStroke(new BasicStroke(1));
        if (lane == 1 || lane == 4) {
            g2d.setColor(new Color(253, 253, 253, 255));
        } else {
            g2d.setColor(new Color(80, 195, 247, 255));
        }

        g2d.fillRect(960 - 450 + (lane * 150), convertNoteTimeToY(time, currentSongTime, scrollSpeed) - 100, 150, 50);
    }

    public void drawLongNote(Graphics2D g2d, int lane, int startTime, int endTime, long currentSongTime) {
        g2d.setStroke(new BasicStroke(1));
        if (lane == 1 || lane == 4) {
            g2d.setColor(new Color(253, 253, 253, 255));
        } else {
            g2d.setColor(new Color(80, 195, 247, 255));
        }

        g2d.fillRect(960 - 450 + (lane * 150),
                getLongNoteEndY(endTime, currentSongTime, scrollSpeed),
                150,
                getLongNoteHeight(startTime, endTime, scrollSpeed));
    }

    public void drawLongNoteHeld(Graphics2D g2d, int lane, int startTime, int endTime, long currentSongTime) {
        g2d.setStroke(new BasicStroke(1));
        if (lane == 1 || lane == 4) {
            g2d.setColor(new Color(253, 253, 253, 255));
        } else {
            g2d.setColor(new Color(80, 195, 247, 255));
        }

        if (currentSongTime < startTime) {
            g2d.fillRect(960 - 450 + (lane * 150),
                    getLongNoteEndY(endTime, currentSongTime, scrollSpeed),
                    150,
                    getLongNoteHeight(startTime, endTime, scrollSpeed));
        } else {
            int y = getLongNoteEndY(endTime, currentSongTime, scrollSpeed);
            g2d.fillRect(960 - 450 + (lane * 150),
                    y,
                    150,
                    1080 - y - 100);
        }
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

    private int getLongNoteHeight(int startTime, int endTime, int scrollSpeed) {
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
            // Return if there are no more notes
            for (int i = 0; i < 4; i++) {
                if (minIndex[i] >= currentGameNotes[i].size()) {
                    return;
                }
            }

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

            handleTiming(timingInaccuracy);

            if (hitOffsets.size() > 25) {
                hitOffsets.remove();
            }
            hitOffsets.add((int) -((nextNote.getStartTime() - Utils.getCurrentSongTime(song))));
            averageHitOffset = (int) (averageHitOffset * 0.6 + (int) -((nextNote.getStartTime() - Utils.getCurrentSongTime(song))) * 0.4);

            nextNote.setHit(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Return if there are no more notes

        int lane = keycodeToLane.getOrDefault(e.getKeyCode(), -1);
        if (lane >= 1 && lane <= 4) {
            laneHeld[lane - 1] = false;

            for (int i = 0; i < 4; i++) {
                if (minIndex[i] >= currentGameNotes[i].size()) {
                    return;
                }
            }

            // Ignore normal note
            if (currentGameNotes[lane - 1].get(minIndex[lane - 1]).getEndTime() == -1) {
                return;
            }

            Note nextNote = currentGameNotes[lane - 1].get(minIndex[lane - 1]);
            // Ignore note since too far away
            int timingInaccuracy = (int) Math.abs(nextNote.getEndTime() - Utils.getCurrentSongTime(song));
            if (timingInaccuracy > 164) {
                return;
            }

            nextNote.setReleased(true);
            minIndex[lane - 1]++;

            if (timingInaccuracy > aaron.rhythm.Utils.getMaxOkayTime()) {
                accuracy = aaron.rhythm.Utils.calculateCumulativeAccuracy(accuracy, Utils.getMissAccuracy(), accuracySampleCount);
                missCount++;
                setHitScore("miss");
            } else {
                handleTiming(timingInaccuracy);
            }

            if (hitOffsets.size() > 25) {
                hitOffsets.remove();
            }

            hitOffsets.add((int) -((nextNote.getEndTime() - Utils.getCurrentSongTime(song))));
            averageHitOffset = (int) (averageHitOffset * 0.6 + (int) -((nextNote.getEndTime() - Utils.getCurrentSongTime(song))) * 0.4);
        }
    }

    private void handleTiming(int timingInaccuracy) {
        if (timingInaccuracy > Utils.getMaxGoodTime()) {
            accuracy = Utils.calculateCumulativeAccuracy(accuracy, Utils.getOkayAccuracy(), accuracySampleCount);
            okayCount++;
            setHitScore("okay");
        } else if (timingInaccuracy > Utils.getMaxGreatTime()) {
            accuracy = Utils.calculateCumulativeAccuracy(accuracy, Utils.getGoodAccuracy(), accuracySampleCount);
            goodCount++;
            setHitScore("good");
        } else if (timingInaccuracy > Utils.getMaxPerfectTime()) {
            accuracy = Utils.calculateCumulativeAccuracy(accuracy, Utils.getGreatAccuracy(), accuracySampleCount);
            greatCount++;
            setHitScore("great");
        } else if (timingInaccuracy > Utils.getMaxMarvelousTime()) {
            accuracy = Utils.calculateCumulativeAccuracy(accuracy, Utils.getPerfectAccuracy(), accuracySampleCount);
            perfectCount++;
            setHitScore("perfect");
        } else {
            accuracy = Utils.calculateCumulativeAccuracy(accuracy, Utils.getMarvelousAccuracy(), accuracySampleCount);
            marvelousCount++;
            setHitScore("marvelous");
        }
    }
}
