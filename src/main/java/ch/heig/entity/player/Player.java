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
import ch.heig.core.render.SpriteRenderUtils;
import ch.heig.core.ressourceManagement.RessourceManager;
import ch.heig.core.utils.DebugUtils;
import ch.heig.core.utils.Vector2f;
import ch.heig.other.Arena;
import ch.heig.entity.SpaceBubble.SpaceBubble;
import ch.heig.entity.bullet.Bullet;

import java.awt.*;
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

    protected Vector2f p_direction = new Vector2f(0,0);        // direction apply to the player
    protected Vector2f p_targetDir =new Vector2f(0,0);          // direction request by the player
    protected double p_rotation =0;
    protected int p_maxNumberDash =2;
    protected int p_maxAmmo =5;
    protected int p_numberDash = p_maxNumberDash;
    protected int p_ammo = p_maxAmmo;
    protected float p_addSpeed =0;

    // state
    protected boolean p_onDash =false;
    protected boolean p_inSpaceBubble =false;

    protected SpaceBubble p_actualBubble;
    private SpaceBubble _bufferActualBubble;


    // input handling
    protected boolean p_hasDash =false;
    protected boolean p_requestDash =false;
    protected boolean p_hasDebug =false;
    protected boolean p_onShoot =false;



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
        return p_direction.copy();
    }


    //#endregion

    public void setSpaceBubble(SpaceBubble bubble){
        _bufferActualBubble=bubble;
    }

    //#region movmement

    protected void startDash(){
        _maxGhostPosition.set(p_position);
        p_position.add(p_targetDir.copy().mult(DASH_DISTANCE));
        p_addSpeed =DASH_ADD_SPEED;
        p_onDash =true;
        p_direction.set(p_targetDir);
    }

    //#endregion

    //#region input handling

    protected void inputUpdate(float delta){
        Point p = getGame().input.getMousePos();
        Vector2f a = getPosition().sub(new Vector2f(p.x,p.y));

        p_rotation =Math.acos(a.normilize().dot(new Vector2f(-1,0)));
        p_rotation *=(a.y<0)?1:-1;

        // input handling
        if(p_hasDash){
            p_hasDash =getGame().input.getMouseRight();
            p_requestDash =false;
        }
        else{
            p_requestDash =getGame().input.getMouseRight();
            p_hasDash = p_requestDash;
        }

        if(p_hasDebug){
            p_hasDebug =getGame().input.getC();
        }
        else if(getGame().input.getC()){
            getGame().debug=(!getGame().debug);
            p_hasDebug =true;
        }


        if(p_onShoot){
            p_onShoot =getGame().input.getMouseLeft();
        }
        else{
            p_onShoot =getGame().input.getMouseLeft();
            if(p_onShoot) {
                createBullet(false);
            }
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
        p_targetDir.set(x,y).normilize();

    }


    //#enregion



    //#region update

    protected void stateUpdate(float deltaTime){

        // add speed / dash state update
        if(p_onDash){
            p_addSpeed -=DASH_ADD_SPEED_DECRESS*deltaTime;
            if(p_addSpeed <=0){
                p_addSpeed =0;
                p_onDash =false;
            }
        }

        p_actualBubble =_bufferActualBubble;

        p_inSpaceBubble =(p_actualBubble !=null);
    }

    private void ghostUpdate(float deltaTime){
        // update ghost position
        // (i know it's a visual and technicly it would be logical to put it on the draw update, but trust me)
        _maxGhostPosition=Vector2f.lerp(_maxGhostPosition, p_position,
                0.03f/((p_onDash)?10:1)*
                        deltaTime
        );
    }

    protected void movementUpdate(float deltaTime){
        // dash
        if(p_requestDash && !p_targetDir.isNull()){
            p_requestDash =false;
            startDash();
            return;
        }

        float velo = (MOVEMENT_SPEED+ p_addSpeed)*deltaTime;


        // Space Bubble
        if(p_inSpaceBubble){
            p_direction.set(p_targetDir);

            if(!p_onDash){
                // get vector
                Vector2f sbVec = getPosition().sub(p_actualBubble.getPosition());
                if(p_targetDir.isNull()) {
                    sbVec.add(sbVec.copy().normilize().mult(collisionRadius));
                }
                else{
                    sbVec.add(p_direction.copy().mult(collisionRadius));
                    sbVec.add(p_direction.copy().mult(velo));
                }

                float m = sbVec.magn();
                float diff = p_actualBubble.collisionRadius-m;

                if(diff<=0){
                    velo=0;
                    p_position.add(
                            getPosition().sub(p_actualBubble.getPosition()).normilize().mult(-1* p_actualBubble.getDecade()*deltaTime)
                    );
                }
            }
        }
        else{
            velo = (SPACE_MOVEMENT_SPEED+ p_addSpeed)*deltaTime;
        }

        if(Arena.active){
            // check disatnce to center
            Vector2f diff = getPosition().sub(Arena.getPosition());
            if(diff.magn()>Arena.radiuse){
                p_direction =diff.normilize().mult(-1);
            }
        }


        if(p_direction.isNull()){
            return;
        }



        // move the player normally
        p_position.add(
                p_direction.copy().mult(velo)
        );

    }

    protected void atUpdateEnd(float deltaTime){

        // rest the space bubble, use to manage collision without a enter / exit hook (i'm lazy)
        _bufferActualBubble=null;
    }



    //#endregion


    //#region Bullet

    private void createBullet(boolean destroyOnContact){
        getGame().createEntity(
                new Bullet(p_position.copy(), destroyOnContact?15:10, destroyOnContact)
                        .setInitSpaceBubble(p_actualBubble)
                        .setVelocity(1, p_rotation)
                , getGroup()
        );
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

        // debug info
        if(getGame().debug){
            g.setColor(Color.magenta);
            g.fillOval((int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);
        }

        BufferedImage newSprite = SpriteRenderUtils.rotateSprite(_sprite, p_rotation +(Math.PI/2));
        Vector2f recenterOffset=new Vector2f(newSprite.getWidth()/3f,newSprite.getHeight()/3f);

        // draw ghost
        Vector2f diff = p_position.copy().sub(_maxGhostPosition);
        if(!diff.isNull()) {
            float step = diff.magn() / _NUMBER_OF_GHOST;
            diff.normilize();
            for (int i = 0; i < _NUMBER_OF_GHOST; i++) {
                // get ghost possition
                Vector2f ghostPos = _maxGhostPosition.copy().add(diff.copy().mult(i * step));

                // change ghost color
                float f = (p_onDash)?0.5f:0.2f;
                float colorFactor =1f;

                if(p_onDash){
                    colorFactor=0.5f+((float) i/_NUMBER_OF_GHOST);
                }
                float[] scales = {f*colorFactor, f*colorFactor, f, 0.3f};
                float[] offsets = {0,0,0,0};
                RescaleOp rop = new RescaleOp(scales,offsets, null);


                // draw ghost
                ((Graphics2D) g).drawImage(newSprite, (BufferedImageOp) rop, (int)(ghostPos.x-recenterOffset.x), (int) (ghostPos.y-recenterOffset.y));
            }
        }
        g.drawImage(newSprite,(int)(p_position.x-recenterOffset.x),(int)(p_position.y-recenterOffset.y),null);

        // debug info
        if(getGame().debug){
            // show state
            Vector2f offset=new Vector2f(0, 50);

            String[] debugInfo=new String[]{
                    "ID : "+getId(),
                    "Position : " + p_position,
                    "Taregt direction : " + p_targetDir,
                    "Direction : " + p_direction,
                    "In Space buble : " + p_inSpaceBubble,
                    "On dash : " + p_onDash
            };

            DebugUtils.drawEntityDebugInfo(g, p_position.copy(),new Vector2f(0, 50),debugInfo);


            g.setColor(Color.GREEN);
            g.fillOval((int) p_position.x,(int) p_position.y,(int)3,(int)3);
            g.setColor(Color.cyan);
            g.fillOval((int)_maxGhostPosition.x,(int)_maxGhostPosition.y,(int)3,(int)3);

            // show aim line
            Vector2f aimingVec = new Vector2f(1,0).rotate(p_rotation).mult(40);
            aimingVec.add(p_position);
            g.setColor(Color.CYAN);
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawLine((int)(p_position.x),(int)(p_position.y),(int)(aimingVec.x),(int)(aimingVec.y));
        }
    }

    @Override
    public int getLayer() {
        return 2;
    }
}
