/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Player
 */

package ch.heig.entity.player;

import ch.heig.core.IUpdatable;
import ch.heig.core.Tag;
import ch.heig.core.collision.CollisionBody;
import ch.heig.core.render.IDrawable;
import ch.heig.core.ressourceManagement.RessourceManager;
import ch.heig.core.utils.DebugUtils;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.Arena;
import ch.heig.entity.SpaceBubble;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;

public class Player extends CollisionBody implements IDrawable, IUpdatable {

    public static float MOVEMENT_SPEED=0.2f;
    public static float SPACE_MOVEMENT_SPEED=0.3f;
    public static float DASH_DISTANCE = 25;
    public static float DASH_ADD_SPEED = 1.5f;
    public static float DASH_ADD_SPEED_DECRESS = 0.02f;

    public final int playerNumber;

    protected Vector2f _direction = new Vector2f(0,0);        // direction apply to the player
    protected Vector2f _targetDir =new Vector2f(0,0);          // direction request by the player
    protected double _rotation=0;
    protected int _maxNumberDash=2;
    protected int _maxAmmo=5;
    protected int _numberDash=_maxNumberDash;
    protected int _ammo=_maxAmmo;
    protected float _addSpeed=0;

    // state
    protected boolean _onDash=false;
    protected boolean _inSpaceBubble=false;

    protected SpaceBubble _actualBubble;


    // input handling
    protected boolean _hasDash=false;
    protected boolean _requestDash=false;
    protected boolean _hasDebug=false;



    // rendering
    private BufferedImage _sprite;
    private Vector2f _maxGhostPosition;
    private static final int _NUMBER_OF_GHOST=3;



    public Player(int playerNumber,Vector2f initPosition) {
        super(initPosition, 12, false);

        this.tag=Tag.player;


        this.playerNumber =playerNumber;
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

    //#region movmement

    protected void startDash(){
        _maxGhostPosition.set(_position);
        _position.add(_targetDir.copy().mult(DASH_DISTANCE));
        _addSpeed=DASH_ADD_SPEED;
        _onDash=true;
        _direction.set(_targetDir);
    }

    //#endregion

    //#region input handling

    protected void inputUpdate(float delta){
        Point p = getGame().input.getMousePos();
        Vector2f a = getPosition().sub(new Vector2f(p.x,p.y));

        _rotation=Math.acos(a.normilize().dot(new Vector2f(-1,0)));
        _rotation*=(a.y<0)?1:-1;

        // input handling
        if(_hasDash){
            _hasDash=getGame().input.getMouseRight();
            _requestDash=false;
        }
        else{
            _requestDash=getGame().input.getMouseRight();
            _hasDash=_requestDash;
        }

        if(_hasDebug){
            _hasDebug=getGame().input.getC();
        }
        else if(getGame().input.getC()){
            getGame().debug=(!getGame().debug);
            _hasDebug=true;
        }


        if(getGame().input.getNum0()){
            getGame().window.gameCanvas.actualGroupRender=0;
            getGame().changeEntityGroup(this,0);
        }

        if(getGame().input.getNum1()){
            getGame().window.gameCanvas.actualGroupRender=1;
            getGame().changeEntityGroup(this,1);
        }






        // define direction
        int x=0;
        int y=0;

        if(getGame().input.getRight()){
            x++;
        }
        if(getGame().input.getLeft()){
            x--;
        }
        if(getGame().input.getUp()){
            y--;
        }
        if(getGame().input.getDown()){
            y++;
        }
        _targetDir.set(x,y).normilize();

    }


    //#enregion



    //#region update

    protected void stateUpdate(float deltaTime){

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

    private void ghostUpdate(float deltaTime){
        // update ghost position
        // (i know it's a visual and technicly it would be logical to put it on the draw update, but trust me)
        _maxGhostPosition=Vector2f.lerp(_maxGhostPosition,_position,
                0.03f/((_onDash)?10:1)*
                        deltaTime
        );
    }

    protected void movementUpdate(float deltaTime){
        // dash
        if(_requestDash && !_targetDir.isNull()){
            _requestDash=false;
            startDash();
            return;
        }

        float velo = (MOVEMENT_SPEED+_addSpeed)*deltaTime;


        // Space Bubble
        if(_inSpaceBubble){
            _direction.set(_targetDir);

            if(!_onDash){
                // get vector
                Vector2f sbVec = getPosition().sub(_actualBubble.getPosition());
                if(_targetDir.isNull()) {
                    sbVec.add(sbVec.copy().normilize().mult(collisionRadius));
                }
                else{
                    sbVec.add(_direction.copy().mult(collisionRadius));
                    sbVec.add(_direction.copy().mult(velo));
                }

                float m = sbVec.magn();
                float diff = _actualBubble.collisionRadius-m;

                if(diff<=0){
                    velo=0;
                    _position.add(
                            getPosition().sub(_actualBubble.getPosition()).normilize().mult(-1*SpaceBubble.RADIUS_DECADE*deltaTime)
                    );
                }
            }
        }
        else{
            velo = (SPACE_MOVEMENT_SPEED+_addSpeed)*deltaTime;
        }

        if(Arena.active){
            // check disatnce to center
            Vector2f diff = getPosition().sub(Arena.getPosition());
            if(diff.magn()>Arena.radiuse){
                _direction=diff.normilize().mult(-1);
            }
        }


        if(_direction.isNull()){
            return;
        }



        // move the player normally
        _position.add(
                _direction.copy().mult(velo)
        );

    }

    protected void atUpdateEnd(float deltaTime){

        // rest the space bubble, use to manage collision without a enter / exit hook (i'm lazy)
        _actualBubble=null;
    }



    //#endregion


    @Override
    public void update(float deltaTime) {
        inputUpdate(deltaTime);
        if(getGame().isServer())stateUpdate(deltaTime);
        ghostUpdate(deltaTime);
        if(getGame().isServer())movementUpdate(deltaTime);
        if(getGame().isServer())atUpdateEnd(deltaTime);

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
        if(getGame().debug){
            g.setColor(Color.magenta);
            g.fillOval((int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);
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
        if(getGame().debug){
            // show state
            Vector2f offset=new Vector2f(0, 50);

            String[] debugInfo=new String[]{
                    "ID : "+getId(),
                    "Position : " + _position,
                    "Taregt direction : " + _targetDir,
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
