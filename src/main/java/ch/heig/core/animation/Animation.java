package ch.heig.core.animation;

import java.util.ArrayList;
import java.util.Arrays;

public class Animation<E> {
    private final KeyFrame<E>[] _keyframes;
    private long _startTime=0;
    private int _currentKeyFrameIndex=0;
    private boolean _run=false;

    public Animation(KeyFrame<E> ... keyFrames){
        this._keyframes=Arrays.copyOf(keyFrames,keyFrames.length);
    }

    protected E betweenFrame(E a, E b, float t){
        return a;
    }

    public void start(){
        _startTime=System.currentTimeMillis();
        _run=true;
        _currentKeyFrameIndex=0;
    }

    public E getValue(){

        if(!_run){
            return _keyframes[_currentKeyFrameIndex].value;
        }

        float time = _startTime-System.currentTimeMillis() / 1000f;

        if(_keyframes[_currentKeyFrameIndex+1].timeStamp<time){
            _currentKeyFrameIndex++;
            if(_currentKeyFrameIndex==_keyframes.length-1){
                _run=false;
                return getValue();
            }
        }
        
        KeyFrame<E> a = _keyframes[_currentKeyFrameIndex];
        KeyFrame<E> b = _keyframes[_currentKeyFrameIndex+1];

        float t =((time / 1000f)-a.timeStamp)/(b.timeStamp-a.timeStamp);

        return betweenFrame(a.value, b.value,t);
    }


    public void stop(){
        _run=false;
    }






}
