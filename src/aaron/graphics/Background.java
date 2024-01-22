// Aaron Ye
// 2024-01-21
// A static background used to imporve performance

package aaron.graphics;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;

// Since drawing images is slow, a separate JPanel is used for a static background
public class Background extends JPanel {
    // THe background
    private BufferedImage background = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    public void setBackground(BufferedImage background) {
        this.background = background;
        repaint();
    }

    /**
     * Clears the background
     */
    public void clearBackground() {
        this.background = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public Background() {
        setLayout(null);
        setOpaque(false);
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        // Draw the background
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
    }
}
