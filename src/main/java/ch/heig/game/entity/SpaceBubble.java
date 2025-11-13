/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Bubble (check design)
 */

package ch.heig.game.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ch.heig.game.core.collision.CollisionBody;
import ch.heig.game.core.render.IDrawable;
import ch.heig.game.core.utils.Vector2f;

public class SpaceBubble extends CollisionBody implements IDrawable {
    public static final float PLAYER_COLLISION_MARGE=5f;

    public SpaceBubble(Vector2f initPosition, float radius) {
        super(initPosition, radius, true);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillOval((int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);

        g.setColor(Color.WHITE);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval((int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);
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


            if(posRelative.magn()+player.collisionRadius - PLAYER_COLLISION_MARGE > collisionRadius){
                return;
            }



            player.setSpaceBubble(this);
        }
    }
}
