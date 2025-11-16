/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Class use to manage collision
 */

package ch.heig.core.collision;

import java.util.ArrayList;

public class CollisionSystem {

    private final ArrayList<CollisionBody> _bodys=new ArrayList<>();            // list of collision body managed

    public CollisionSystem(){

    }


    /**
     * register a new collsion body
     * @param body
     */
    public void registerBody(CollisionBody body){
        _bodys.add(body);
    }

    /**
     * unregister a new collsion body
     * @param body
     */
    public void unregisterBody(CollisionBody body){
        _bodys.remove(body);
    }


    /**
     * Do the collsion update on every collsion body register
     */
    public void doCollisionUpdate(){
        for (int i = 0; i < _bodys.size(); i++) {
            for (int j = i+1; j < _bodys.size(); j++) {
                _bodys.get(i).doCollision(_bodys.get(j));
            }
        }
    }
}
