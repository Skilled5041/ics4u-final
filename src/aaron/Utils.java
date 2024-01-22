// Aaron Ye
// 2024-01-21

package aaron;

public class Utils {
    /**
     * Forces a value to be between a min and max
     * @param value The value
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static double Clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}
