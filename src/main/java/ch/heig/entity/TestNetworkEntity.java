package ch.heig.entity;

import ch.heig.core.IUpdatable;
import ch.heig.core.Tag;
import ch.heig.core.collision.CollisionBody;
import ch.heig.network.networkHandler.INetworkReceiverEntity;
import ch.heig.network.networkHandler.INetworkSenderEntity;
import ch.heig.core.render.IDrawable;
import ch.heig.core.utils.Vector2f;
import ch.heig.network.packet.data.CollisionBodyData;
import ch.heig.network.packet.data.EntityData;
import ch.heig.network.packet.data.PacketData;
import ch.heig.network.packet.data.PacketDataType;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TestNetworkEntity extends CollisionBody implements IDrawable, IUpdatable, INetworkSenderEntity, INetworkReceiverEntity {

    private boolean _isActive=false;
    public int live = 2;
    private boolean _hasActive=false;

    public TestNetworkEntity(Vector2f initPosition, float collisionRadius) {
        super(initPosition, collisionRadius, true);
    }

    @Override
    public void draw(Graphics g) {
        Color col = (_isActive)?Color.RED:Color.GREEN;
        g.setColor(col);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawOval((int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius),(int) collisionRadius *2,(int) collisionRadius *2);

        BufferedImage face =new BufferedImage(
                (int)(50),
                (int)(50),
                BufferedImage.TYPE_INT_ARGB
        );


        Graphics2D g2 = face.createGraphics();
        AffineTransform at = new AffineTransform();
        //at.rotate(2);
        at.rotate((Math.PI/2), 25, 25);
        g2.transform(at);
        g2.setStroke(new BasicStroke(5));
        g2.setColor(col);
        g2.drawString((_isActive)?">:(":":)",20,27);
        g2.dispose();
        g.drawImage(face,(int)(_position.x)-25,(int)(_position.y)-25,null);

        if(!getGame().debug)return;
        g.drawString("ID : "+getId()+" | Live : " + live,(int)(_position.x- collisionRadius),(int)(_position.y- collisionRadius));

    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void update(float deltaTime) {
        if(!getGame().isServer())return;

        if(_hasActive){
            _hasActive=_isActive;
        }
        else if(_isActive){
            _hasActive=true;
            live--;
            if(live<0){
                getGame().destroyEntity(this);
            }
        }

        _isActive=false;
    }


    @Override
    public void onTrigger(CollisionBody other) {
        if(other.tag==Tag.player){
            _isActive=true;
        }
    }


    public static class Data extends CollisionBodyData {
        public boolean isActive=false;
        public float radius=0f;

        public Data(TestNetworkEntity ent){
            super(ent);
            isActive=ent._isActive;
            radius=ent.collisionRadius;
            this.type=PacketDataType.TestNetworkEntity;
        }
    }


    @Override
    public EntityData getData() {
        return new TestNetworkEntity.Data(this);
    }

    @Override
    public void applyData(PacketData data) {
        if(data.type == PacketDataType.TestNetworkEntity){
            _isActive = ((TestNetworkEntity.Data)data).isActive;
        }
    }
}
