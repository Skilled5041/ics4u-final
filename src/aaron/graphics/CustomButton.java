// Aaron Ye
// 2024-01-21

package aaron.graphics;

import aaron.Game;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import static aaron.graphics.Utils.drawCenteredString;

public class CustomButton extends JComponent implements MouseListener {
    public final int width;
    public final int height;
    public final String label;
    private Font font = Game.fontMedium;
    public void setFont(Font font) {
        this.font = font;
    }
    // The current background colour
    private Color currentBackground = new Color(52, 52, 52, 255);
    // The base background colour
    private Color normalBackground = new Color(52, 52, 52, 255);
    public void setBackground(Color bg) {
        this.normalBackground = bg;
    }
    public CustomButton(int width, int height, String label) {
        this.width = width;
        this.height = height;
        this.label = label;

        setOpaque(false);
        setSize(width, height);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);

        // Background
        g2d.setColor(currentBackground);
        g2d.fillRect(0, 0, width, height);
        // Outline
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(5, 134, 228, 255));
        g2d.drawRect(0, 0, width, height);
        // Label
        drawCenteredString(g2d, width / 2, height / 2, label);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            addMouseListener(this);
        } else {
            removeMouseListener(this);
        }
        // Change background colour
        currentBackground = normalBackground;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Change background colour
        currentBackground = new Color(58, 94, 114, 255);
        onClick.accept(e);
    }

    private Consumer<MouseEvent> onClick = e -> {};
    public void onClick(Consumer<MouseEvent> handler) {
        onClick = handler;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Change background colour
        currentBackground = new Color(58, 94, 114, 255);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Change background colour
        currentBackground = normalBackground;
    }
}
