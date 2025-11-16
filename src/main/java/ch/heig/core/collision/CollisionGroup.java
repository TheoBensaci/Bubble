package ch.heig.core.collision;

import java.util.ArrayList;

public class CollisionGroup {
    public final int index;
    public final ArrayList<CollisionBody> bodies=new ArrayList<>();

    public CollisionGroup(int index){
        this.index=index;
    }

    /**
     * Do the collsion update on every collsion body register
     */
    public void doCollisionUpdate(){
        for (int i = 0; i < bodies.size(); i++) {
            for (int j = i+1; j < bodies.size(); j++) {
                bodies.get(i).doCollision(bodies.get(j));
            }
        }
    }
}
