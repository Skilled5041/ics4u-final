package aaron.screens;

import aaron.Game;
import aaron.Main;
import aaron.SettingsManager;
import aaron.graphics.CustomButton;
import aaron.graphics.Screen;

import javax.sound.sampled.Clip;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

public class Settings implements Screen {
    // Used to change settings
    private static final JCheckBox hitGlowCheckBox = new JCheckBox("Enable Hit Glow");
    private static final JCheckBox lineSeparatorCheckBox = new JCheckBox("Enable Lane Separators");
    private static final SpinnerModel scrollSpeedModel = new SpinnerNumberModel(SettingsManager.getScrollSpeed(), 1, 100, 1);
    private static final JSpinner scrollSpeed = new JSpinner(scrollSpeedModel);
    private static final SpinnerModel offsetModel = new SpinnerNumberModel(SettingsManager.getScrollSpeed(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    private static final JSpinner offset = new JSpinner(offsetModel);
    private static final CustomButton lane1Color = new CustomButton(800, 200, "Lane 1 Color");
    private static final CustomButton lane2Color = new CustomButton(800, 200, "Lane 2 Color");
    private static final CustomButton lane3Color = new CustomButton(800, 200, "Lane 3 Color");
    private static final CustomButton lane4Color = new CustomButton(800, 200, "Lane 4 Color");
    private static final CustomButton backButton = new CustomButton(800, 200, "Back");

    /**
     * Avoids null error
     */
    public static void init() {
        // Set location of components
        hitGlowCheckBox.setBounds(850, 100, 200, 40);
        lineSeparatorCheckBox.setBounds(850, 150, 200, 40);
        scrollSpeed.setBounds(850, 200, 200, 40);
        offset.setBounds(850, 250, 200, 40);
        lane1Color.setBounds(850, 300, 200, 40);
        lane2Color.setBounds(850, 350, 200, 40);
        lane3Color.setBounds(850, 400, 200, 40);
        lane4Color.setBounds(850, 450, 200, 40);
        backButton.setLocation(1920 / 2 - 400, 1080 / 2 + 300);

        Main.game.add(hitGlowCheckBox);
        Main.game.add(lineSeparatorCheckBox);
        Main.game.add(scrollSpeed);
        Main.game.add(offset);
        Main.game.add(lane1Color);
        Main.game.add(lane2Color);
        Main.game.add(lane3Color);
        Main.game.add(lane4Color);
        Main.game.add(backButton);

        hitGlowCheckBox.setVisible(false);
        lineSeparatorCheckBox.setVisible(false);
        scrollSpeed.setVisible(false);
        offset.setVisible(false);
        lane1Color.setVisible(false);
        lane2Color.setVisible(false);
        lane3Color.setVisible(false);
        lane4Color.setVisible(false);
        backButton.setVisible(false);

        // Set up handlers
        backButton.onClick(e -> {
            Main.game.switchScreen(Game.Screens.MAIN_MENU);
            SettingsManager.save();
        });

        lane1Color.onClick(e -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", SettingsManager.getLane1Color());
            if (color != null) {
                SettingsManager.setLane1Color(color);
            }
            lane1Color.setBackground(SettingsManager.getLane1Color());
        });

        lane2Color.onClick(e -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", SettingsManager.getLane2Color());
            if (color != null) {
                SettingsManager.setLane2Color(color);
            }
            lane2Color.setBackground(SettingsManager.getLane2Color());
        });

        lane3Color.onClick(e -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", SettingsManager.getLane3Color());
            if (color != null) {
                SettingsManager.setLane3Color(color);
            }
            lane3Color.setBackground(SettingsManager.getLane3Color());
        });

        lane4Color.onClick(e -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", SettingsManager.getLane4Color());
            if (color != null) {
                SettingsManager.setLane4Color(color);
            }
            lane4Color.setBackground(SettingsManager.getLane4Color());
        });

        hitGlowCheckBox.addActionListener(e -> SettingsManager.setHitGlow(hitGlowCheckBox.isSelected()));
        lineSeparatorCheckBox.addActionListener(e -> SettingsManager.setLaneSeparators(lineSeparatorCheckBox.isSelected()));
        scrollSpeed.addChangeListener(e -> SettingsManager.setScrollSpeed((int) scrollSpeed.getValue()));
        offset.addChangeListener(e -> SettingsManager.setOffset((int) offset.getValue()));


        lane1Color.setBackground(SettingsManager.getLane1Color());
        lane2Color.setBackground(SettingsManager.getLane2Color());
        lane3Color.setBackground(SettingsManager.getLane3Color());
        lane4Color.setBackground(SettingsManager.getLane4Color());
    }

    @Override
    public void start() {
        // Make them visible and set them to the correct values
        hitGlowCheckBox.setSelected(SettingsManager.getHitGlow());
        lineSeparatorCheckBox.setSelected(SettingsManager.getLaneSeparators());
        scrollSpeed.setValue(SettingsManager.getScrollSpeed());
        offset.setValue(SettingsManager.getOffset());

        hitGlowCheckBox.setVisible(true);
        lineSeparatorCheckBox.setVisible(true);
        scrollSpeed.setVisible(true);
        offset.setVisible(true);
        lane1Color.setVisible(true);
        lane2Color.setVisible(true);
        lane3Color.setVisible(true);
        lane4Color.setVisible(true);
        backButton.setVisible(true);

        MainMenu.menuLoop.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void end() {
        hitGlowCheckBox.setVisible(false);
        lineSeparatorCheckBox.setVisible(false);
        scrollSpeed.setVisible(false);
        offset.setVisible(false);
        lane1Color.setVisible(false);
        lane2Color.setVisible(false);
        lane3Color.setVisible(false);
        lane4Color.setVisible(false);
        backButton.setVisible(false);

        MainMenu.menuLoop.stop();
    }

    @Override
    public void render(Graphics2D g2d) {
        // Labels
        g2d.setFont(Game.fontSmall);
        g2d.drawString("Hit Glow", 600, 130);
        g2d.drawString("Lane Separators", 600, 180);
        g2d.drawString("Scroll Speed", 600, 230);
        g2d.drawString("Offset", 600, 280);
        g2d.drawString("Lane 1 Color", 600, 330);
        g2d.drawString("Lane 2 Color", 600, 380);
        g2d.drawString("Lane 3 Color", 600, 430);
        g2d.drawString("Lane 4 Color", 600, 480);
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
