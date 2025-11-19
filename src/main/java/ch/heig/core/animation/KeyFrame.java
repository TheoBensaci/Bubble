package ch.heig.core.animation;

public class KeyFrame<E> {
    public final E value;
    public final float timeStamp;

    public KeyFrame(E value, float timeStamp){
        this.value=value;
        this.timeStamp=timeStamp;
    }
}
