// Aaron Ye
// 2024-01-21

package aaron;

import aaron.graphics.Background;
import aaron.screens.About;
import aaron.screens.ChartSelect;
import aaron.screens.MainMenu;
import aaron.screens.Result;
import aaron.screens.Settings;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    // Create JFrames and JPanels
    public static JFrame frame = new JFrame("Game");
    public static Background background = new Background();
    public static Game game = new Game();

    public static void main(String[] args) {
        // Initialize all screens
        Result.init();
        MainMenu.init();
        ChartSelect.init();
        Settings.init();
        About.init();
        background.setBounds(0, 0, Game.WIDTH, Game.HEIGHT);
        game.setBounds(0, 0, Game.WIDTH, Game.HEIGHT);

        // Set up the window
        game.start();
        game.setLayout(null);
        frame.setLayout(null);
        frame.add(game);
        frame.add(background);
        frame.setSize(Game.WIDTH, Game.HEIGHT);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        // Exit the program when the window is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.onExit();
                System.exit(0);
            }
        });
        frame.setVisible(true);

        // Save settings on exit
        Runtime.getRuntime().addShutdownHook(new Thread(SettingsManager::save));
    }
}