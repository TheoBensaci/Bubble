package ch.heig.entity;

import ch.heig.core.Entity;
import ch.heig.core.render.IDrawable;
import ch.heig.core.utils.Vector2f;

import java.awt.*;

public class Arena extends Entity implements IDrawable {

    public final float radiuse;
    public static Arena actualArena;
    private final Vector2f _position=new Vector2f(0,0);

    public Arena(Vector2f position,float radiuse){
        this.radiuse=radiuse;
        _position.set(position);
        if(actualArena==null){
            actualArena=this;
        }
    }

    @Override
    public void markAsDestroy() {
        super.markAsDestroy();
        if(actualArena==this){
            actualArena=null;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval((int)(_position.x- radiuse),(int)(_position.y- radiuse),(int) radiuse *2,(int) radiuse *2);
    }

    @Override
    public int getLayer() {
        return -1;
    }


    public Vector2f getPosition(){
        return _position.copy();
    }
}
