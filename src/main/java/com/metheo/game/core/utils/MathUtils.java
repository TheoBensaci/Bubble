package ch.heig.game.core.utils;

public class MathUtils {

    /**
     * interpolate between a and b
     * @param a
     * @param b
     * @param t [0-1]
     * @return
     */
    public static float lerp(float a, float b, float t){
        return a*(1-t)+b*t;
    }
}
