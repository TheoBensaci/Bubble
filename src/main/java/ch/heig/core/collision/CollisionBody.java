/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Entity implementing basic collision
 */

package ch.heig.core.collision;

import ch.heig.core.Entity;
import ch.heig.core.utils.Vector2f;

public class CollisionBody extends Entity{

    protected final Vector2f _position;                     // position
    public float collisionRadius;
    public boolean collisionTrigger;
    public boolean collisionEnable =true;

    public CollisionBody(Vector2f initPosition, float collisionRadius, boolean initTrigger){
        _position=initPosition;
        this.collisionRadius =collisionRadius;
        collisionTrigger =initTrigger;
    }

    /**
     * Get a copy of the actual position of this instance
     * @return
     */
    public Vector2f getPosition(){
        return _position.copy();
    }

    /**
     * Set the position of this instance
     * @param position
     */
    public void setPosition(Vector2f position){
        _position.set(position);
    }

    /**
     * Set the position of this instance
     * @param x
     * @param y
     */
    public void setPosition(float x, float y){
        _position.set(x,y);
    }

    /**
     * Do the collision with a other collision body
     * A collision is : Check distance -> collision resolution
     * @param other other collision body
     */
    public void doCollision(CollisionBody other){
        if(!collisionEnable || !other.collisionEnable)return;
        Vector2f diff = other.getPosition().sub(getPosition());
        float distance = diff.magn();

        if(distance<(collisionRadius +other.collisionRadius)){
            // trigger gestion
            if(collisionTrigger || other.collisionTrigger){
                if(collisionTrigger)onTrigger(other);
                if(other.collisionTrigger)other.onTrigger(this);
                return;
            }
            diff.normilize();
            distance-= collisionRadius +other.collisionRadius;
            diff.mult(distance);
            _position.add(diff);
            other._position.sub(diff);
        }
    }

    /**
     * Call when 2 collision body collide
     * @param other
     */
    public void onCollision(CollisionBody other){

    }

    /**
     * Call if this instance is set to trigger and it collide with a other collision body
     * @param other
     */
    public void onTrigger(CollisionBody other){

    }


}
