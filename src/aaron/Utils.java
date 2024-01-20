package aaron;

public class Utils {
    public static double Clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}
