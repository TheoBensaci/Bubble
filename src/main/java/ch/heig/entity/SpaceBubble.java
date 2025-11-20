/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Bubble (check design)
 */

package ch.heig.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ch.heig.core.IUpdatable;
import ch.heig.core.collision.CollisionBody;
import ch.heig.entity.player.Player;
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

    private final float _maxRadiuse;

    private float _actualDecade=0;

    private boolean _hasPlayer=false;

    private boolean _isActive=true;

    private boolean _receveDamage=false;

    public SpaceBubble(Vector2f initPosition, float radius) {
        super(initPosition, radius, true);
        _maxRadiuse=radius;
    }

    public SpaceBubble(SpaceBubble.Data data) {
        this(new Vector2f(data.positionX,data.positionY),data.radius);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillOval((int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);

        g.setColor(Color.WHITE);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval((int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);

        if(!getGame().debug)return;
        g.drawString("ID : "+getId(),(int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius));
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


            if(posRelative.magn() + player.collisionRadius/4  > collisionRadius){
                return;
            }

            _hasPlayer=true;





            player.setSpaceBubble(this);
        }
    }

    @Override
    public void update(float deltaTime) {

        if(!getGame().isServer())return;

        if((_hasPlayer || _receveDamage)){
            applyDecade(deltaTime);
        }
        else{
            _actualDecade=0;
        }

        if(!_hasPlayer){
            collisionRadius = (collisionRadius>=_maxRadiuse)?_maxRadiuse:collisionRadius+RADIUS_REGENERATE*deltaTime;
        }

        _hasPlayer=false;
    }

    @Override
    public void applyData(PacketData data) {
        SpaceBubble.Data d = (SpaceBubble.Data)data;
        collisionRadius=d.radius;
        _position.x=d.positionX;
        _position.y=d.positionY;
    }

    @Override
    public EntityData getData() {
        return new Data(this);
    }


    public float getDecade(){
        return _actualDecade;
    }

    public void damage(){
        _actualDecade=RADIUS_DECADE*1000;
        _receveDamage=true;
    }

    private void applyDecade(float deltaTime){
        if(collisionRadius==0)return;
        applyDamage(_actualDecade*deltaTime);
        _actualDecade=RADIUS_DECADE;
        collisionRadius=(collisionRadius<0)?0:collisionRadius;
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
            isActive=true;
            radius=ent.collisionRadius;
            this.type= PacketDataType.Bubble;
        }
    }
}
