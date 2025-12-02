/**
 *   Autheur: Theo Bensaci
 *   Date: 10:16 24.11.2025
 *   Description: Animation which lerp value between frame (use float value)
 */

package ch.heig.core.animation.basicAnimation;

import ch.heig.core.animation.Animation;
import ch.heig.core.animation.KeyFrame;
import ch.heig.core.utils.MathUtils;

public class LerpAnimation extends Animation<Float> {

    @SafeVarargs
    public LerpAnimation(KeyFrame<Float>... keyFrames){
        super(keyFrames);
    }
    @Override
    protected Float betweenFrame(Float a, Float b, float t) {
        return MathUtils.lerp(a,b,t);
    }
}
