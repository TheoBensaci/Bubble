package ch.heig.entity.bullet;

import ch.heig.core.IUpdatable;
import ch.heig.core.collision.CollisionBody;
import ch.heig.core.render.IDrawable;
import ch.heig.core.render.SpriteRenderUtils;
import ch.heig.core.ressourceManagement.RessourceManager;
import ch.heig.core.utils.Vector2f;
import ch.heig.network.packet.data.CollisionBodyData;
import ch.heig.network.packet.data.PacketDataType;
import ch.heig.other.Arena;
import ch.heig.entity.SpaceBubble.SpaceBubble;
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

    private BufferedImage _sprite;

    private Player _owner;

    public Bullet(Bullet.Data data) {
        this(new Vector2f(data.positionX,data.positionY),data.radius,false);
        _rotation=data.rotation;
    }


    public Bullet(Vector2f position, float size, boolean destroyOnContact) {
        super(position, size, true);
        _velocity=new Vector2f(0,0);
        this.destroyOnContact=destroyOnContact;
        _sprite = RessourceManager.getTexture("textures/bullet.png");
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

    public static class Data extends CollisionBodyData {
        public float radius=0f;
        public float rotation=0f;

        public Data(Bullet ent){
            super(ent);
            rotation=(float)ent._rotation;
            radius=ent.collisionRadius;
            this.type= PacketDataType.Bullet;
        }
    }

    @Override
    public void applyData(PacketData data) {
        Data bData = (Bullet.Data)data;
        p_position.set(bData.positionX,bData.positionY);
        _rotation=bData.rotation;
        collisionRadius=bData.radius;
    }

    @Override
    public EntityData getData() {
        return new Data(this);
    }

    @Override
    public void draw(Graphics g) {

        // debug info
        if(getGame().debug){
            g.setColor(Color.magenta);
            g.fillOval((int)(p_position.x- collisionRadius),(int)(p_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);
        }
        float scale = collisionRadius/6;

        BufferedImage rotatedSprite=SpriteRenderUtils.rotateSprite(_sprite,_rotation);

        int w = (int)(rotatedSprite.getWidth()*scale);
        int h = (int)(rotatedSprite.getHeight()*scale);

        Vector2f recenterOffset=new Vector2f(w, h);
        recenterOffset.mult(0.5f);
        BufferedImage img =new BufferedImage(
                w,
                h,
                BufferedImage.TYPE_INT_ARGB
        );


        Graphics2D g2 = img.createGraphics();
        AffineTransform at = new AffineTransform();
        at.scale(scale,scale);
        g2.transform(at);
        g2.drawImage(rotatedSprite,0,0,null);
        g2.dispose();
        g.drawImage(img,(int)(p_position.x-recenterOffset.x),(int)(p_position.y-recenterOffset.y),null);
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

        p_position.add(_velocity.copy().mult(deltaTime));

        if(Arena.active){
            // check distance to center
            Vector2f diff = getPosition().sub(Arena.getPosition());
            if(diff.magn()>Arena.radiuse){
                getGame().destroyEntity(this);
                return;
            }
        }
        else{
            if(p_position.magn()>1000){
                getGame().destroyEntity(this);
            }
        }
    }

    @Override
    public void onTrigger(CollisionBody other) {
        if(other instanceof SpaceBubble bubble){
            if(bubble==_initSpaceBubble || !bubble.isActive())return;
            bubble.damage();
            kill();
        }

        if(other instanceof Player player){
            if(player==_owner)return;
            // kill the player >:[
        }
    }
}
