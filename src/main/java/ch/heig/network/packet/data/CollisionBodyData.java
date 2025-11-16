package ch.heig.network.packet.data;

import ch.heig.core.collision.CollisionBody;
import ch.heig.core.utils.Vector2f;

public class CollisionBodyData extends EntityData {
    public float positionX=0;
    public float positionY=0;

    public CollisionBodyData(CollisionBody collisionBody){
        super(collisionBody);
        Vector2f p = collisionBody.getPosition();
        positionX=p.x;
        positionY=p.y;
    }
}
