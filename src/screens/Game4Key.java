package screens;

import charts.Chart;
import charts.Note;
import graphics.Screen;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game4Key implements Screen {
    @Override
    public void start() throws LineUnavailableException, IOException {
        chart = Chart.parse("charts/1", "12278.qua");
        song = chart.getAudio();
        song.start();
    }

    @Override
    public void end() {

    }

    private Chart chart;
    private Clip song;
    private final int scrollSpeed = 20;
    private int minIndex = 0;

    @Override
    public void render(BufferedImage buffer) {
        Graphics2D g2d = buffer.createGraphics();

        // Main play area
        g2d.setStroke(new BasicStroke(2));
        graphics.Utils.drawCenteredRectangle(g2d, 960, 540, 600, 1082);
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
            if (note.startTime() < currentSongTime) {
                minIndex = i + 1;
                continue;
            }
            if (note.endTime() > currentSongTime + rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed)) {
                break;
            }
            drawNote(g2d, note.Lane(), note.startTime(), currentSongTime);
        }
    }

    private void drawNote(Graphics2D g2d, int lane, int time, long currentSongTime) {
        g2d.setStroke(new BasicStroke(1));
        if (lane == 1 || lane == 4) {
            g2d.setColor(new Color(253, 253, 253, 255));
        } else {
            g2d.setColor(new Color(80, 195, 247, 255));
        }

        g2d.fillRect(960 - 450 + (lane * 150), convertNoteTimeToY(time, currentSongTime, scrollSpeed), 150, 50);
    }

    private static int convertNoteTimeToY(int time, long currentSongTime, int scrollSpeed) {
        int scrollSpeedTimeWidth = rhythm.Utils.scrollSpeedToTimeWidth(scrollSpeed);
        double decimal = (double) (time - currentSongTime) / scrollSpeedTimeWidth;
        return (int) (1080 - decimal * 1080);
    }

    public void drawLongNote(int lane) {

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
