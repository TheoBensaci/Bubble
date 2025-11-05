package com.metheo.game.entity;

import com.metheo.game.core.Game;
import com.metheo.game.core.IUpdateable;
import com.metheo.game.core.collision.CollisionBody;
import com.metheo.game.core.render.IDrawable;
import com.metheo.game.core.utils.Input;
import com.metheo.game.core.utils.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.Objects;

public class Player extends CollisionBody implements IDrawable, IUpdateable {

    public static float MOVEMENT_SPEED=0.2f;
    public static float DASH_DISTANCE = 25;
    public static float DASH_ADD_SPEED = 1.5f;
    public static float DASH_ADD_SPEED_DECRESS = 0.02f;

    public final int PlayerNumber;

    private Vector2f _direction = new Vector2f(0,0);
    private double _rotation=0;
    private int _maxNumberDash=2;
    private int _maxAmmo=5;
    private int _numberDash=_maxNumberDash;
    private int _ammo=_maxAmmo;
    private float _addSpeed=0;
    private boolean _onDash=false;


    // input handling
    private boolean _hasDash=false;



    // rendering
    private BufferedImage _sprite;
    private Vector2f _maxGhostPosition;
    private static final int _NUMBER_OF_GHOST=3;



    public Player(int playerNumber,Vector2f initPosition) {
        super(initPosition, 24, false);
        PlayerNumber=playerNumber;
        _maxGhostPosition=initPosition.copy();

        // get the sprite

        try{
            _sprite = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("textures/player"+playerNumber+".png")));
        }catch(IOException e){e.printStackTrace();}
    }

    //#region GET
    public Vector2f getDirection(){
        return _direction.copy();
    }


    //#endregion


    @Override
    public void update(float deltaTime) {
        Point p = Input.getMousePos();
        Vector2f a = getPosition().sub(new Vector2f(p.x,p.y));

        _rotation=Math.acos(a.normilize().dot(new Vector2f(-1,0)));
        _rotation*=(a.y<0)?1:-1;

        // update ghost position
        // (i know it's a visual and technicly it would be logical to put it on the draw update, but trust me)
        _maxGhostPosition=Vector2f.lerp(_maxGhostPosition,_position,
                0.03f/((_onDash)?10:1)*
                deltaTime
        );


        // input handling
        if(_hasDash){
            _hasDash=Input.getMouseRight();
        }


        // define direction
        int x=0;
        int y=0;

        if(Input.getRight()){
            x++;
        }
        if(Input.getLeft()){
            x--;
        }
        if(Input.getUp()){
            y--;
        }
        if(Input.getDown()){
            y++;
        }

        if(Input.getMouseLeft() || Input.getC()){
            _position.x=x;
            _position.y=y;
        }

        _direction.x=x;
        _direction.y=y;


        // skip if no move
        if(!_direction.isNull()){
            _direction.normilize();
        }

        // dash
        if(!_hasDash && Input.getMouseRight() && !_direction.isNull()){
            _hasDash=true;
            _position.add(_direction.copy().mult(DASH_DISTANCE));
            _addSpeed=DASH_ADD_SPEED;
            _onDash=true;
            return;
        }

        // add speed update
        if(_onDash){
            _addSpeed-=DASH_ADD_SPEED_DECRESS*deltaTime;
            if(_addSpeed<=0){
                _addSpeed=0;
                _onDash=false;
            }
        }


        if(_direction.isNull()){
           return;
        }



        // move the player normally
        _position.add(_direction.copy().mult((MOVEMENT_SPEED+_addSpeed)*deltaTime));
    }

    @Override
    public void draw(Graphics g) {



        if(_sprite==null){
            return;
        }
        int w = _sprite.getWidth();
        int h = _sprite.getHeight();
        Vector2f recenterOffset=new Vector2f((float) w /2, (float) h /2);

        // debug info
        if(Game.DEBUG){
            g.setColor(Color.YELLOW);
            g.fillOval((int)(_position.x-CollisionRadius/2),(int)(_position.y-CollisionRadius/2),(int)CollisionRadius,(int)CollisionRadius);
        }

        BufferedImage img =new BufferedImage(
                (int)(w*1.5f),
                (int)(h*1.5f),
                BufferedImage.TYPE_INT_ARGB
        );


        Graphics2D g2 = img.createGraphics();
        AffineTransform at = new AffineTransform();
        //at.rotate(2);
        at.rotate(_rotation+(Math.PI/2), recenterOffset.x, recenterOffset.y);
        g2.transform(at);
        g2.drawImage(_sprite,0,0,null);

        g2.dispose();

        // draw ghost
        Vector2f diff = _position.copy().sub(_maxGhostPosition);
        if(!diff.isNull()) {
            float step = diff.magn() / _NUMBER_OF_GHOST;
            diff.normilize();
            for (int i = 0; i < _NUMBER_OF_GHOST; i++) {
                Vector2f ghostPos = _maxGhostPosition.copy().add(diff.copy().mult(i * step));
                float f = (_onDash)?0.5f:0.2f;
                float[] scales = {f, f, f, 0.3f};
                float[] offsets = new float[4];
                RescaleOp rop = new RescaleOp(scales, offsets, null);
                ((Graphics2D) g).drawImage(img, (BufferedImageOp) rop, (int)(ghostPos.x-recenterOffset.x), (int) (ghostPos.y-recenterOffset.y));
            }
        }
        g.drawImage(img,(int)(_position.x-recenterOffset.x),(int)(_position.y-recenterOffset.y),null);

        // debug info
        if(Game.DEBUG){
            g.setColor(Color.GREEN);
            g.fillOval((int)_position.x,(int)_position.y,(int)3,(int)3);
            g.setColor(Color.cyan);
            g.fillOval((int)_maxGhostPosition.x,(int)_maxGhostPosition.y,(int)3,(int)3);
        }
    }

    @Override
    public int getLayer() {
        return 0;
    }
}
