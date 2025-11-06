package com.metheo.game.entity;

import com.metheo.game.core.collision.CollisionBody;
import com.metheo.game.core.render.IDrawable;
import com.metheo.game.core.utils.Vector2f;

import java.awt.*;

public class SpaceBubble extends CollisionBody implements IDrawable {

    public static final float PLAYER_COLLISION_MARGE=5f;

    public SpaceBubble(Vector2f initPosition, float radius) {
        super(initPosition, radius, true);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillOval((int)(_position.x-CollisionRadius),(int)(_position.y-CollisionRadius),(int)CollisionRadius*2,(int)CollisionRadius*2);

        g.setColor(Color.WHITE);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval((int)(_position.x-CollisionRadius),(int)(_position.y-CollisionRadius),(int)CollisionRadius*2,(int)CollisionRadius*2);
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void onTrigger(CollisionBody oder) {
        if(oder instanceof Player player){
            // check if it strickly in the bubble or not
            Vector2f posRelative = player.getPosition().sub(_position);


            if(posRelative.magn()+player.CollisionRadius - PLAYER_COLLISION_MARGE >CollisionRadius){
                return;
            }



            player.setSpaceBubble(this);
        }
    }
}
