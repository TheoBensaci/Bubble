package ch.heig.entity;

import ch.heig.core.Entity;
import ch.heig.core.render.IDrawable;
import ch.heig.core.utils.Vector2f;

import java.awt.*;

public class Arena {
    public static boolean active=true;
    public static float radiuse=200f;
    public static Vector2f position=new Vector2f(0,0);


    public static void draw(Graphics g) {
        g.setColor(Color.WHITE);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.fillRect((int)(position.x- 5),(int)(position.x- 5),10,10);
        g.drawOval((int)(position.x- radiuse),(int)(position.y- radiuse),(int) radiuse *2,(int) radiuse *2);
    }

    public static Vector2f getPosition(){
        return position.copy();
    }
}
