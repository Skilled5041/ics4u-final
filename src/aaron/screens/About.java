package aaron.screens;

import aaron.Game;
import aaron.Main;
import aaron.graphics.CustomButton;
import aaron.graphics.Screen;

import javax.sound.sampled.Clip;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

public class About implements Screen {
    private static final CustomButton backButton = new CustomButton(800, 200, "Back");

    public static void init() {
        backButton.setLocation(1920 / 2 - 400, 1080 / 2 + 300);
        backButton.onClick(e -> Main.game.switchScreen(Game.Screens.MAIN_MENU));
        Main.game.add(backButton);
        backButton.setVisible(false);
    }

    @Override
    public void start() {
        backButton.setVisible(true);
        MainMenu.menuLoop.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void end() {
        backButton.setVisible(false);
        MainMenu.menuLoop.stop();
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setFont(Game.fontSmaller);
        g2d.drawString("Name: Aaron Ye", 500, 200);
        g2d.drawString("Date: 2024-01-21", 500, 240);
        g2d.drawString("Instructions: hit the notes to the beat", 500, 280);
        g2d.drawString("Controls: A, S, K, L for hitting notes, use scroll wheel or up/down arrows to scroll through charts.", 500, 320);
        g2d.drawString("Use escape to exit the game. Use scroll wheel to change the scroll speed when in-game.", 500, 360);
        g2d.drawString("Settings can be used to change graphics settings and offset.", 500, 400);
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
