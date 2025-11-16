/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Math utils
 */

package ch.heig.core.utils;

public class MathUtils {

    /**
     * interpolate between a and b
     * @param a value a
     * @param b value b
     * @param t t value [0-1]
     * @return value lerp on t
     */
    public static float lerp(float a, float b, float t){
        return a*(1-t)+b*t;
    }
}
