
package ch.heig.core.animation;

import java.util.ArrayList;
import java.util.Arrays;

public class Animation<E> {
    private final KeyFrame<E>[] _keyframes;
    private long _startTime=0;
    private int _currentKeyFrameIndex=0;
    private boolean _run=false;

    @SafeVarargs
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

    public boolean isStop(){
        return !_run;
    }

    private int getIndexByTime(float time){
        for (int i = 0; i < _keyframes.length; i++) {
            if(_keyframes[i].timeStamp>time){
                return i-1;
            }
        }
        return _keyframes.length-1;
    }

    public E getValue(){

        if(!_run){
            return _keyframes[_currentKeyFrameIndex].value;
        }

        float time = (float)(System.currentTimeMillis() - _startTime)/1000f;

        int index = getIndexByTime(time);
        if(index==_keyframes.length-1){
            _run=false;
            return getValue();
        }

        int index2=index+1;
        
        KeyFrame<E> a = _keyframes[index];
        KeyFrame<E> b = _keyframes[index2];

        float t =(time-a.timeStamp)/(b.timeStamp-a.timeStamp);

        return betweenFrame(a.value, b.value,t);
    }


    public void stop(){
        _run=false;
    }






}
