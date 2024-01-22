// Aaron Ye
// 2024-01-21

package aaron.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Utils {

    /**
     * Draws a fill rectangle centered at a point
     * @param g2d Graphics2D object
     * @param x x coordinate of center
     * @param y y coordinate of center
     * @param width width of rectangle
     * @param height height of rectangle
     */
    public static void drawCenteredFilledRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.fillRect(x - width / 2, y - height / 2, width, height);
    }

    /**
     * Draws a rectangle centered at a point
     * @param g2d Graphics2D object
     * @param x x coordinate of center
     * @param y y coordinate of center
     * @param width width of rectangle
     * @param height height of rectangle
     */
    public static void drawCenteredRectangle(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawRect(x - width / 2, y - height / 2, width, height);
    }

    /**
     * Draws an image centered at a point
     * @param g2d Graphics2D object
     * @param x x coordinate of center
     * @param y y coordinate of center
     * @param image BufferedImage object
     */
    public static void drawCenteredImage(Graphics2D g2d, int x, int y, BufferedImage image, double scale) {
        g2d.drawImage(image, x - (int) (image.getWidth() * scale / 2), y - (int) (image.getHeight() * scale / 2), (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
    }

    /**
     * Draws a string centered at a point
     * @param g2d Graphics2D object
     * @param x x coordinate of center
     * @param y y coordinate of center
     * @param text String object
     */
    public static void drawCenteredString(Graphics2D g2d, int x, int y, String text) {
        int width = g2d.getFontMetrics().stringWidth(text);
        int height = g2d.getFontMetrics().getHeight();
        g2d.drawString(text, x - width / 2, y + height / 4);
    }
}
