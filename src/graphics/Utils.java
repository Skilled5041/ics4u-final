package graphics;

import java.awt.Graphics2D;

public class Utils {
    public static void drawCenteredFilledRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.fillRect(x - width / 2, y - height / 2, width, height);
    }

    public static void drawCenteredRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawRect(x - width / 2, y - height / 2, width, height);
    }
}
