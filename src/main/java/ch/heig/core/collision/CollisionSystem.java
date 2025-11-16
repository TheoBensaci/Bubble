/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Class use to manage collision
 */

package ch.heig.core.collision;

import ch.heig.core.Entity;

import java.util.ArrayList;

public class CollisionSystem {

    private final ArrayList<CollisionBody> _bodys=new ArrayList<>();            // list of collision body managed
    private final ArrayList<CollisionGroup> _groups=new ArrayList<>();

    public CollisionSystem(){

    }


    /**
     * register a new collsion body
     * @param body
     */
    public void registerBody(CollisionBody body){
        //_bodys.add(body);
        for (CollisionGroup group : _groups){
            if(group.index==body.getGroup()){
                group.bodies.add(body);
                return;
            }
        }
        CollisionGroup group = new CollisionGroup(body.getGroup());
        group.bodies.add(body);
        _groups.add(group);
    }

    /**
     * unregister a new collsion body
     * @param body
     */
    public void unregisterBody(CollisionBody body){
        for (CollisionGroup group : _groups){
            if(group.bodies.contains(body)){
                group.bodies.remove(body);
                if(group.bodies.isEmpty()){
                    _groups.remove(group);
                }
                return;
            }
        }
    }


    /**
     * Do the collsion update on every collsion body register
     */
    public void doCollisionUpdate(){
        for (CollisionGroup group : _groups){
            group.doCollisionUpdate();
        }
    }

    public void updateBodyGroup(CollisionBody body){
        unregisterBody(body);
        registerBody(body);
    }

    private CollisionGroup getCollisionGroup(int index){
        for (CollisionGroup group : _groups){
            if(group.index==index)return group;
        }
        return null;
    }
}
