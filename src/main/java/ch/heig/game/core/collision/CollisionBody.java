package ch.heig.game.core.collision;

import ch.heig.game.core.Entity;
import ch.heig.game.core.utils.Vector2f;

public class CollisionBody extends Entity {

    protected final Vector2f _position;
    public final float collisionRadius;
    public boolean trigger;

    public CollisionBody(Vector2f initPosition, float collisionRadius, boolean initTrigger){
        _position=initPosition;
        this.collisionRadius =collisionRadius;
        trigger =initTrigger;
    }

    public Vector2f getPosition(){
        return _position.copy();
    }

    public void setPosition(Vector2f vec){
        _position.set(vec);
    }

    public void setPosition(float x, float y){
        _position.set(x,y);
    }

    public void collisionStepWith(CollisionBody oder){
        Vector2f diff = oder.getPosition().sub(getPosition());
        float distance = diff.magn();

        if(distance<(collisionRadius +oder.collisionRadius)){
            // trigger gestion
            if(trigger || oder.trigger){
                if(trigger)onTrigger(oder);
                if(oder.trigger)oder.onTrigger(this);
                return;
            }
            diff.normilize();
            distance-= collisionRadius +oder.collisionRadius;
            diff.mult(distance);
            _position.add(diff);
            oder._position.sub(diff);
        }
    }

    public void onCollision(CollisionBody oder){

    }

    public void onTrigger(CollisionBody oder){

    }


}
