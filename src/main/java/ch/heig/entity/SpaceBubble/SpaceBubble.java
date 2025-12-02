/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Bubble (check design)
 */

package ch.heig.entity.SpaceBubble;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import ch.heig.core.IUpdatable;
import ch.heig.core.collision.CollisionBody;
import ch.heig.entity.player.Player;
import ch.heig.entity.player.ServerPlayer;
import ch.heig.entity.simpleEffect.SimpleEffect;
import ch.heig.network.networkHandler.INetworkReceiverEntity;
import ch.heig.network.networkHandler.INetworkSenderEntity;
import ch.heig.core.render.IDrawable;
import ch.heig.core.utils.Vector2f;
import ch.heig.network.packet.data.CollisionBodyData;
import ch.heig.network.packet.data.EntityData;
import ch.heig.network.packet.data.PacketData;
import ch.heig.network.packet.data.PacketDataType;

public class SpaceBubble extends CollisionBody implements IDrawable, IUpdatable, INetworkSenderEntity, INetworkReceiverEntity {
    public static final float PLAYER_COLLISION_MARGE=5f;
    public final static float RADIUS_DECADE=0.015f;
    public final static float RADIUS_REGENERATE=0.05f;
    public final static float INACTIVE_RADIUS_REGENERATE=0.015f;
    public final static float MIN_RADIUS=10f;
    public final static float INACTIVE_TIME=1000f;
    public final static float DAMAGE_VALUE=10f;

    private final float _maxRadiuse;

    private float _actualDecade=0;

    private boolean _hasPlayer=false;

    private boolean _isActive=true;

    private boolean _receveDamage=false;

    private float _inActiveTimer=0f;

    private float _lastRadius=0;        // use to guess bubble hit by a bullet

    public SpaceBubble(Vector2f initPosition, float radius) {
        super(initPosition, radius, true);
        _maxRadiuse=radius;
    }

    public SpaceBubble(SpaceBubble.Data data) {
        this(new Vector2f(data.positionX,data.positionY),data.radius);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor((_isActive)?Color.BLACK:new Color(0x11121B));
        g.fillOval((int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);

        g.setColor((_isActive)?Color.WHITE:Color.darkGray);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval((int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);

        if(!getGame().debug)return;
        g.drawString("ID : "+getId(),(int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius));
        g.drawString("R : "+collisionRadius,(int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius)+20);
        g.drawString("draw layer : "+getLayer(),(int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius)+40);
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void onTrigger(CollisionBody oder) {
        if(oder instanceof Player player){


            // check if it strickly in the bubble or not
            Vector2f posRelative = player.getPosition().sub(p_position);


            if(posRelative.magn() + player.collisionRadius/4  > collisionRadius){
                return;
            }

            _hasPlayer=true;

            if(!_isActive)return;

            player.setSpaceBubble(this);
        }
    }

    @Override
    public void update(float deltaTime) {

        if(!getGame().isServer())return;

        if(_isActive) {
            _actualDecade = RADIUS_DECADE;
            if(_receveDamage){
                _actualDecade=(_lastRadius-collisionRadius)/deltaTime;
                _receveDamage=false;
            }
            else if(_hasPlayer){
                applyDamage(_actualDecade*deltaTime);
                collisionRadius=(collisionRadius<0)?0:collisionRadius;
            }else {
                _actualDecade = 0;
            }

            _isActive=(collisionRadius>MIN_RADIUS);
            if(!_isActive){
                _inActiveTimer=INACTIVE_TIME;
            }
        }
        else if(_inActiveTimer>0f){
            _inActiveTimer-=deltaTime;
            return;
        }

        if(!_hasPlayer){
            float newRad = collisionRadius+(_isActive?RADIUS_REGENERATE:INACTIVE_RADIUS_REGENERATE)*deltaTime;
            if(newRad>=_maxRadiuse){
                collisionRadius = _maxRadiuse;
                _isActive=true;
            }
            else{
                collisionRadius = newRad;
            }
        }

        _lastRadius=collisionRadius;

        _hasPlayer=false;
    }

    @Override
    public void applyData(PacketData data) {
        SpaceBubble.Data d = data.safeCast( SpaceBubble.Data.class);
        if(d==null)return;
        collisionRadius=d.radius;
        p_position.x=d.positionX;
        p_position.y=d.positionY;
        _isActive=d.isActive;
    }

    @Override
    public EntityData getData() {
        return new Data(this);
    }


    public float getDecade(){
        return _actualDecade;
    }

    public void damage(){
        applyDamage(DAMAGE_VALUE);
        _receveDamage=true;
    }


    private void applyDamage(float amount){
        if(collisionRadius==0)return;
        collisionRadius-=amount;
        collisionRadius=(collisionRadius<0)?0:collisionRadius;
    }

    public boolean isActive(){
        return _isActive;
    }


    public static class Data extends CollisionBodyData {
        public boolean isActive=false;
        public float radius=0f;

        public Data(SpaceBubble ent){
            super(ent);
            isActive=ent._isActive;
            radius=ent.collisionRadius;
            this.type= PacketDataType.Bubble;
        }
    }


    public void createPart(Vector2f position){
        Random rand = new Random();
        getGame().createEntity(
                new SimpleEffect("textures/bubble_out.png",
                        position,
                        128,
                        128,
                        15,
                        0.5f,
                        rand.nextDouble(-1*Math.PI,Math.PI)
                ),
                getGroup()
        );
    }
}
