package ch.heig.game.entity;

import ch.heig.game.core.IUpdateable;
import ch.heig.game.core.collision.CollisionBody;
import ch.heig.game.core.render.GameRender;
import ch.heig.game.core.render.IDrawable;
import ch.heig.game.core.utils.Input;
import ch.heig.game.core.utils.Vector2f;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TestObj extends CollisionBody implements IDrawable, IUpdateable {
    private Vector2f _velocity=new Vector2f(1,0);
    private float _speed=0.1f;
    private double _rotation=0;
    private boolean test=false;

    public static final double ROTATION_ANGLE=0.2d;
    public static final double DRIFT_ANGLE_MODIFIER=0.75f;
    public static final float DRIFT_SPEED_MODIFIER=0.75f;

    public TestObj(boolean t){
        super(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2 + 10),10,true);
        test=t;
    }


    @Override
    public void draw(Graphics g) {
        int w = (int)(30*1.5f);

        BufferedImage img =new BufferedImage(
                w,
                w,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = img.createGraphics();
        AffineTransform at = new AffineTransform();
        //at.rotate(2);
        at.rotate(_rotation,w / 2, w / 2);
        at.translate((w) / 4, (w) / 4);
        g2.transform(at);

        g2.setColor(new Color(0xff0055));
        g2.fillRect(0,0,20,20);
        g2.setColor(new Color(0x00ff99));
        g2.fillRect(10,5,20,10);

        g2.dispose();
        g.drawImage(img,(int)_position.x,(int)_position.y,null);
        g.setColor(Color.YELLOW);
        Vector2f a = getPosition().sub(new Vector2f(Input.getMousePos().x,Input.getMousePos().y));
        g.drawString("p : "+a.x+" | "+a.y,(int)_position.x,(int)_position.y+30);
        g.drawString("mouse : "+Input.getMousePos().x+" | "+Input.getMousePos().y,(int)_position.x,(int)_position.y+60);
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void update(float deltaTime) {
        Point p = Input.getMousePos();
        Vector2f a = getPosition().sub(new Vector2f(p.x,p.y));

        _rotation=Math.acos(a.normilize().dot(new Vector2f(-1,0)));
        _rotation*=(a.y<0)?1:-1;

        if(test)return;
        if(Input.getRight()){
            _position.add(new Vector2f(deltaTime,0));
        }
        if(Input.getLeft()){
            _position.add(new Vector2f(-deltaTime,0));
        }
        if(Input.getA()){
            TestObj t =  new TestObj(false);
            t.destroy();
        }

        //_rotation = Math.acos(new Vector2f(p.x,p.y).copy().sub(_position).dot(new Vector2f(1,0)));

        /*
        double angle = 0;
        float speed = _speed;



        if(Input.getRight()){
            angle+=ROTATION_ANGLE;
        }

        if(Input.getLeft()){
            angle-=ROTATION_ANGLE;
        }


        if(Input.getA()){
            speed*=DRIFT_SPEED_MODIFIER;
            angle*=DRIFT_ANGLE_MODIFIER;
        }

        if(Input.getC()){
            _position=new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2);
            return;
        }

        angle*=deltaTime;

        _rotation+=angle;



        if(angle!=0){
            Vector2f buf = _velocity.copy();
            buf.rotate(Math.toRadians(angle));
            _velocity=buf;
        }

        speed*=deltaTime;

        _position.add(_velocity.copy().mult(speed));*/
    }


}
