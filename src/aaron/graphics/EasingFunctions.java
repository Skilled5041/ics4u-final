package aaron.graphics;

public class EasingFunctions {
    // https://easings.net/#easeOutExpo
    public static double easeOutExpo(double n) {
        return n == 1 ? 1 : 1 - Math.pow(2, -10 * n);
    }

    // https://easings.net/#easeInOutExpo
    public static double easeInOutExpo(double n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if ((n *= 2) < 1) {
            return 0.5 * Math.pow(2, 10 * (n - 1));
        }
        return 0.5 * (-Math.pow(2, -10 * --n) + 2);
    }
}
