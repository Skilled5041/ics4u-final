package aaron;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static JFrame frame = new JFrame("Game");
    public static Game game = new Game();

    public static void main(String[] args) {
        game.start();
        frame.add(game);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.onExit();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}