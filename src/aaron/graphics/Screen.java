package aaron.graphics;

import java.awt.Graphics2D;
import java.awt.event.KeyListener;

public interface Screen extends KeyListener {
    /**
     * Called when the game transitions to this state
     */
    void start();

    /**
     * Called when the game transitions away from this state
     */
    void end();

    /**
     * Called every frame to render the state
     *
     * @param g2d Graphics2D object
     */
    void render(Graphics2D g2d);
}
