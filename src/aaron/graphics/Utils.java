package aaron.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Utils {

    public static void drawCenteredFilledRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.fillRect(x - width / 2, y - height / 2, width, height);
    }

    public static void drawCenteredRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawRect(x - width / 2, y - height / 2, width, height);
    }

    public static void drawCenteredImage(Graphics2D g2d, int x, int y, BufferedImage image, double scale) {
        g2d.drawImage(image, x - (int) (image.getWidth() * scale / 2), y - (int) (image.getHeight() * scale / 2), (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
    }
}
