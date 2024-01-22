package aaron.rhythm;

import javax.sound.sampled.Clip;

public class Utils {
    public static int scrollSpeedToTimeWidth(int scrollSpeed) {
        return 5000 / scrollSpeed;
    }

    public static double calculateCumulativeAccuracy(double previousAccuracy, double newAccuracy, int newCount) {
        double result = Math.max(0, previousAccuracy * (newCount - 1) / newCount + newAccuracy / newCount);
        if (Double.isNaN(result)) {
            return 0;
        }
        return result;
    }

    public static long getCurrentSongTime(Clip song) {
        return song.getLongFramePosition() * 1000 / (long) song.getFormat().getFrameRate();
    }

    public static int getMaxOkayTime() {
        return 127;
    }

    public static int getMaxGoodTime() {
        return 106;
    }

    public static int getMaxGreatTime() {
        return 76;
    }

    public static int getMaxPerfectTime() {
        return 43;
    }

    public static int getMaxMarvelousTime() {
        return 18;
    }

    public static double getMarvelousAccuracy() {
        return 1;
    }

    public static double getPerfectAccuracy() {
        return 1;
    }

    public static double getGreatAccuracy() {
        return 0.65;
    }

    public static double getGoodAccuracy() {
        return 0.25;
    }

    // On the screen website where I got this accuracy system from, "okays" are worse than misses
    public static double getOkayAccuracy() {
        return -1;
    }

    public static double getMissAccuracy() {
        return -0.5;
    }
}
