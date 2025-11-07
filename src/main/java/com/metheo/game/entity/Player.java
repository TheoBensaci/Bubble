package com.metheo.game.entity;

import com.metheo.game.core.Game;
import com.metheo.game.core.IUpdateable;
import com.metheo.game.core.collision.CollisionBody;
import com.metheo.game.core.render.IDrawable;
import com.metheo.game.core.ressourceManagement.RessourceManager;
import com.metheo.game.core.utils.DebugUtils;
import com.metheo.game.core.utils.Input;
import com.metheo.game.core.utils.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends CollisionBody implements IDrawable, IUpdateable {

    public static float MOVEMENT_SPEED=0.2f;
    public static float SPACE_MOVEMENT_SPEED=0.4f;
    public static float DASH_DISTANCE = 25;
    public static float DASH_ADD_SPEED = 1.5f;
    public static float DASH_ADD_SPEED_DECRESS = 0.02f;

    public final int PlayerNumber;

    private Vector2f _direction = new Vector2f(0,0);        // direction apply to the player
    private Vector2f _tragetDir=new Vector2f(0,0);          // direction request by the player
    private double _rotation=0;
    private int _maxNumberDash=2;
    private int _maxAmmo=5;
    private int _numberDash=_maxNumberDash;
    private int _ammo=_maxAmmo;
    private float _addSpeed=0;

    // state
    private boolean _onDash=false;
    private boolean _inSpaceBubble=false;

    private SpaceBubble _actualBubble;


    // input handling
    private boolean _hasDash=false;
    private boolean _hasDebug=false;



    // rendering
    private BufferedImage _sprite;
    private Vector2f _maxGhostPosition;
    private static final int _NUMBER_OF_GHOST=3;



    public Player(int playerNumber,Vector2f initPosition) {
        super(initPosition, 12, false);
        PlayerNumber=playerNumber;
        _maxGhostPosition=initPosition.copy();

        // get the sprite
        _sprite= RessourceManager.getTexture("textures/player"+playerNumber+".png");

    }

    //#region GET
    public Vector2f getDirection(){
        return _direction.copy();
    }


    //#endregion

    public void setSpaceBubble(SpaceBubble bubble){
        _actualBubble=bubble;
    }

    // "phyisc"





    //#region update

    protected void stateUpdate(float deltaTime){
        // input handling
        if(_hasDash){
            _hasDash=Input.getMouseRight();
        }

        if(_hasDebug){
            _hasDebug=Input.getC();
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
        _tragetDir.set(x,y).normilize();


        // add speed / dash state update
        if(_onDash){
            _addSpeed-=DASH_ADD_SPEED_DECRESS*deltaTime;
            if(_addSpeed<=0){
                _addSpeed=0;
                _onDash=false;
            }
        }

        _inSpaceBubble=(_actualBubble!=null);
    }

    protected void movementUpdate(float deltaTime){
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

        // dash
        if(!_hasDash && Input.getMouseRight() && !_tragetDir.isNull()){
            _hasDash=true;
            _maxGhostPosition.set(_position);
            _position.add(_tragetDir.copy().mult(DASH_DISTANCE));
            _addSpeed=DASH_ADD_SPEED;
            _onDash=true;
            _direction.set(_tragetDir);
            return;
        }


        if(_inSpaceBubble){
            _direction.set(_tragetDir);
        }



        if(_direction.isNull()){
            return;
        }



        // move the player normally
        _position.add(
                _direction.copy().mult((MOVEMENT_SPEED+_addSpeed)*deltaTime)
        );

        // Space Bubble constraint

        if(_inSpaceBubble){
            // TODO : correct the position to restraint the player inside the bubble
        }

    }

    protected void atUpdateEnd(float deltaTime){

        // rest the space bubble, use to manage collision without a enter / exit hook (i'm lazy)
        _actualBubble=null;
    }



    //#endregion


    @Override
    public void update(float deltaTime) {

        stateUpdate(deltaTime);
        movementUpdate(deltaTime);
        atUpdateEnd(deltaTime);

        if(!_hasDebug && Input.getC()){
            Game.DEBUG=(!Game.DEBUG);
            _hasDebug=true;
        }
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
            g.setColor(Color.magenta);
            g.fillOval((int)(_position.x-CollisionRadius),(int)(_position.y-CollisionRadius),(int)CollisionRadius*2,(int)CollisionRadius*2);
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
                // get ghost possition
                Vector2f ghostPos = _maxGhostPosition.copy().add(diff.copy().mult(i * step));

                // change ghost color
                float f = (_onDash)?0.5f:0.2f;
                float colorFactor =1f;

                if(_onDash){
                    colorFactor=0.5f+((float) i/_NUMBER_OF_GHOST);
                }
                float[] scales = {f*colorFactor, f*colorFactor, f, 0.3f};
                float[] offsets = {0,0,0,0};
                RescaleOp rop = new RescaleOp(scales,offsets, null);


                // draw ghost
                ((Graphics2D) g).drawImage(img, (BufferedImageOp) rop, (int)(ghostPos.x-recenterOffset.x), (int) (ghostPos.y-recenterOffset.y));
            }
        }
        g.drawImage(img,(int)(_position.x-recenterOffset.x),(int)(_position.y-recenterOffset.y),null);

        // debug info
        if(Game.DEBUG){
            // show state
            Vector2f offset=new Vector2f(0, 50);

            String[] debugInfo=new String[]{
                    "Position : " + _position,
                    "Taregt direction : " + _tragetDir,
                    "Direction : " + _direction,
                    "In Space buble : " + _inSpaceBubble,
                    "On dash : " + _onDash
            };

            DebugUtils.drawEntityDebugInfo(g,_position.copy(),new Vector2f(0, 50),debugInfo);


            g.setColor(Color.GREEN);
            g.fillOval((int)_position.x,(int)_position.y,(int)3,(int)3);
            g.setColor(Color.cyan);
            g.fillOval((int)_maxGhostPosition.x,(int)_maxGhostPosition.y,(int)3,(int)3);

            // show aim line
            Vector2f aimingVec = new Vector2f(1,0).rotate(_rotation).mult(40);
            aimingVec.add(_position);
            g.setColor(Color.CYAN);
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawLine((int)(_position.x),(int)(_position.y),(int)(aimingVec.x),(int)(aimingVec.y));
        }
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
