package graphics;

import javax.sound.sampled.LineUnavailableException;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface Screen extends KeyListener {
    /**
     * Called when the game transitions to this state
     */
    void start() throws LineUnavailableException, IOException;

    /**
     * Called when the game transitions away from this state
     */
    void end();

    /**
     * Called every frame to render the state
     *
     * @param bufferedImage bufferedImage object
     */
    void render(BufferedImage bufferedImage);
}
