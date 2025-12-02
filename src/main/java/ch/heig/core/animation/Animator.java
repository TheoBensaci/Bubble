/**
 *   Autheur: Theo Bensaci
 *   Date: 10:10 21.11.2025
 *   Description: Class use to manage a list of animation, useful when we deal with multiple state of animation
 */

package ch.heig.core.animation;

import java.util.ArrayList;
import java.util.List;

public class Animator<E> {
    private final List<Animation<E>> _animations=new ArrayList<>();
    private int _currentAnimation=0;
    private boolean _run = false;


    public Animator(){
        _run=false;
    }


    public int registerAnimation(Animation<E> newAnimation){
        _animations.add(newAnimation);
        return _animations.size()-1;
    }

    public E getValue(){
        return _animations.get(_currentAnimation).getValue();
    }

    public boolean isRunning(){
        return _run;
    }

    public void playAnimation(int index){
        if(index<0 || index>_animations.size())return;
        _currentAnimation=index;
        _animations.get(index).start();
    }

    public void stopAnimation(){

    }
}
