package com.metheo.game.core.collision;

import com.metheo.game.core.Entity;
import com.metheo.game.core.Game;
import com.metheo.game.core.utils.Vector2f;

public class CollisionBody extends Entity {

    protected final Vector2f _position;
    public final float CollisionRadius;
    public boolean Trigger;

    public CollisionBody(Vector2f initPosition, float collisionRadius, boolean initTrigger){
        _position=initPosition;
        CollisionRadius=collisionRadius;
        Trigger=initTrigger;
    }

    public Vector2f getPosition(){
        return _position.copy();
    }

    public void collisionStepWith(CollisionBody oder){
        Vector2f diff = oder.getPosition().sub(getPosition());
        float distance = diff.magn();

        if(distance<(CollisionRadius+oder.CollisionRadius)){
            // trigger gestion
            if(Trigger || oder.Trigger){
                if(Trigger)onTrigger(oder);
                if(oder.Trigger)oder.onTrigger(this);
                return;
            }
            diff.normilize();
            distance-=CollisionRadius+oder.CollisionRadius;
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
