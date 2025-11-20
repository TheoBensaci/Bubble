package ch.heig.entity.bullet;

import ch.heig.core.IUpdatable;
import ch.heig.core.collision.CollisionBody;
import ch.heig.core.render.IDrawable;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.Arena;
import ch.heig.entity.SpaceBubble;
import ch.heig.entity.player.Player;
import ch.heig.network.networkHandler.INetworkReceiverEntity;
import ch.heig.network.networkHandler.INetworkSenderEntity;
import ch.heig.network.packet.data.EntityData;
import ch.heig.network.packet.data.PacketData;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends CollisionBody implements IUpdatable,IDrawable,INetworkReceiverEntity, INetworkSenderEntity {


    private final Vector2f _velocity;
    private double _rotation=0;
    public final boolean destroyOnContact;

    private SpaceBubble _initSpaceBubble;

    private Player _owner;



    public Bullet(Vector2f position, float size, boolean destroyOnContact) {
        super(position, size, true);
        _velocity=new Vector2f(0,0);
        this.destroyOnContact=destroyOnContact;
    }

    // builder

    public Bullet setInitSpaceBubble(SpaceBubble bubble){
        _initSpaceBubble=bubble;
        return this;
    }


    public Bullet setOwner(Player player){
        _owner=player;
        return this;
    }

    public Bullet setVelocity(float velocity, double rad){
        _rotation=rad;
        _velocity.set(new Vector2f(1,0).rotate(rad).mult(velocity));
        return this;
    }

    @Override
    public void applyData(PacketData data) {

    }

    @Override
    public EntityData getData() {
        return null;
    }

    @Override
    public void draw(Graphics g) {
        int w = (int)(collisionRadius*1.5f);
        int h = (int)(collisionRadius*2f);
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
        g2.setColor(Color.YELLOW);
        g2.fillOval(0,0,(int)(collisionRadius*1.5f),(int)(collisionRadius*2f));
        //g2.drawImage(_sprite,0,0,null);
        g2.dispose();
        g.drawImage(img,(int)(_position.x-recenterOffset.x),(int)(_position.y-recenterOffset.y),null);
    }

    @Override
    public int getLayer() {
        return 4;
    }


    public void kill(){
        getGame().destroyEntity(this);
    }

    @Override
    public void update(float deltaTime) {
        if(!getGame().isServer())return;

        _position.add(_velocity.copy().mult(deltaTime));

        if(Arena.active){
            // check disatnce to center
            Vector2f diff = getPosition().sub(Arena.getPosition());
            if(diff.magn()>Arena.radiuse){
                getGame().destroyEntity(this);
                return;
            }
        }
        else{
            if(_position.magn()>1000){
                getGame().destroyEntity(this);
            }
        }
    }

    @Override
    public void onTrigger(CollisionBody other) {
        if(other instanceof SpaceBubble bubble){
            if(bubble==_initSpaceBubble)return;
            bubble.damage();
            kill();
        }

        if(other instanceof Player player){
            if(player==_owner)return;
            // kill the player >:[
        }
    }
}
