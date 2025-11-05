package ch.heig.game.entity;

import ch.heig.game.core.collision.CollisionBody;
import ch.heig.game.core.render.IDrawable;
import ch.heig.game.core.utils.Vector2f;

import java.awt.*;

public class SpaceBubble extends CollisionBody implements IDrawable {


    public SpaceBubble(Vector2f initPosition, float radius) {
        super(initPosition, radius, true);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawOval((int)(_position.x-CollisionRadius/2),(int)(_position.y-CollisionRadius/2),(int)CollisionRadius,(int)CollisionRadius);
    }

    @Override
    public int getLayer() {
        return 0;
    }
}
