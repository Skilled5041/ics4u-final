package aaron;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// File in the format of KEY=VALUE
public class SettingsManager {
    private static final String SETTINGS_FILE = ".conf";

    private static int scrollSpeed;
    private static boolean hitGlow;
    private static boolean laneSeparators;
    private static int offset;
    private static Color lane1Color;
    private static Color lane2Color;
    private static Color lane3Color;
    private static Color lane4Color;

    public static int getScrollSpeed() {
        return scrollSpeed;
    }

    public static boolean getHitGlow() {
        return hitGlow;
    }

    public static boolean getLaneSeparators() {
        return laneSeparators;
    }

    public static int getOffset() {
        return offset;
    }

    public static Color getLane1Color() {
        return lane1Color;
    }

    public static Color getLane2Color() {
        return lane2Color;
    }

    public static Color getLane3Color() {
        return lane3Color;
    }

    public static Color getLane4Color() {
        return lane4Color;
    }

    public static void setScrollSpeed(int scrollSpeed) {
        SettingsManager.scrollSpeed = scrollSpeed;
    }

    public static void setHitGlow(boolean hitGlow) {
        SettingsManager.hitGlow = hitGlow;
    }

    public static void setLaneSeparators(boolean laneSeparators) {
        SettingsManager.laneSeparators = laneSeparators;
    }

    public static void setOffset(int offset) {
        SettingsManager.offset = offset;
    }

    public static void setLane1Color(Color lane1Color) {
        SettingsManager.lane1Color = lane1Color;
    }

    public static void setLane2Color(Color lane2Color) {
        SettingsManager.lane2Color = lane2Color;
    }

    public static void setLane3Color(Color lane3Color) {
        SettingsManager.lane3Color = lane3Color;
    }

    public static void setLane4Color(Color lane4Color) {
        SettingsManager.lane4Color = lane4Color;
    }

    public static void save() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(SETTINGS_FILE);
        } catch (Exception e) {
            System.out.println("Error opening settings!");
            System.exit(1);
        }

        try {
            writer.write("SCROLL_SPEED=" + scrollSpeed + "\n");
            writer.write("HIT_GLOW=" + hitGlow + "\n");
            writer.write("LANE_SEPARATORS=" + laneSeparators + "\n");
            writer.write("OFFSET=" + offset + "\n");
            writer.write("LANE_1_COL=" + lane1Color.getRGB() + "\n");
            writer.write("LANE_2_COL=" + lane2Color.getRGB() + "\n");
            writer.write("LANE_3_COL=" + lane3Color.getRGB() + "\n");
            writer.write("LANE_4_COL=" + lane4Color.getRGB() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving settings!");
            System.exit(1);
        }
    }

    static {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(SETTINGS_FILE));
        } catch (FileNotFoundException e) {
            System.out.println("Settings file not found!");
            System.exit(1);
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split("=");
            String key = split[0];
            String value = split[1];

            switch (key) {
                case "SCROLL_SPEED":
                    scrollSpeed = Integer.parseInt(value);
                    break;
                case "HIT_GLOW":
                    hitGlow = Boolean.parseBoolean(value);
                    break;
                case "LANE_SEPARATORS":
                    laneSeparators = Boolean.parseBoolean(value);
                    break;
                case "OFFSET":
                    offset = Integer.parseInt(value);
                    break;
                case "LANE_1_COL":
                    lane1Color = Color.decode(value);
                    break;
                case "LANE_2_COL":
                    lane2Color = Color.decode(value);
                    break;
                case "LANE_3_COL":
                    lane3Color = Color.decode(value);
                    break;
                case "LANE_4_COL":
                    lane4Color = Color.decode(value);
                    break;
            }
        }
    }
}
