package work.lclpnet.ryuo.util;

public class Ticks {

    public static long nearestEnclosingMillis(long millis) {
        return (long) Math.ceil(millis / 50D);
    }

    public static long seconds(int seconds) {
        return seconds * 20L;
    }

    public static long minutes(int minutes) {
        return seconds(minutes * 60);
    }
}
