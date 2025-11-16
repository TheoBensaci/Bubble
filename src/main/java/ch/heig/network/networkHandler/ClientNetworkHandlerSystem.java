/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Client network handler
 */

package ch.heig.network.networkHandler;

import ch.heig.core.Entity;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.ClientPlayer;
import ch.heig.entity.player.ServerPlayer;
import ch.heig.network.coreVariant.ClientGame;
import ch.heig.entity.SpaceBubble;
import ch.heig.entity.TestNetworkEntity;
import ch.heig.network.socket.GameSocket;
import ch.heig.network.packet.GameStatePacket;
import ch.heig.network.packet.Packet;
import ch.heig.network.packet.PacketType;
import ch.heig.network.packet.data.EntityData;
import ch.heig.network.packet.data.PacketDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientNetworkHandlerSystem extends NetworkHandlerSystem{

    private final ArrayList<Entity> _serverOwnedEntity=new ArrayList<>();

    private int _gameStateNumber=-1;

    private float _lastServerUpdateClock=0;


    public ClientNetworkHandlerSystem() {
        super();
    }

    public void receiveUpdate(GameSocket socket){
        Packet[] buffer=new Packet[0];
        try{
            socket.mutex.acquire();
            if(socket.receivedPackets.isEmpty()){
                socket.mutex.release();

                _lastServerUpdateClock+=_game.getDeltaTime();

                if(_lastServerUpdateClock>1000){
                    _game.close();
                    return;
                }
                return;
            }

            buffer = new Packet[socket.receivedPackets.size()];
            socket.receivedPackets.toArray(buffer);
            socket.receivedPackets.clear();
            socket.mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        _lastServerUpdateClock=0;

        for (Packet p : buffer) {
            if(p.type== PacketType.gameState){
                GameStatePacket gsp = (GameStatePacket) p;
                if(gsp.number<=_gameStateNumber && (gsp.number/_gameStateNumber)>0)return;
                _gameStateNumber=gsp.number;
                applyGameState(gsp);
            }
        }
    }


    public void senderUpdate(GameSocket socket){
        ClientGame cg = (ClientGame) _game;
        if(cg.player==null)return;
        if(cg.player.getClock() < 50)return;
        socket.send(cg.player.createPacket());
    }


    private void applyGameState(GameStatePacket gsp){
        boolean[] needToDestroy = new boolean[_recever.size()];
        Arrays.fill(needToDestroy,true);

        for (EntityData data : gsp.data){
            boolean found = false;
            for (int i = 0; i < _recever.size(); i++) {
                INetworkReceiverEntity n = _recever.get(i);
                Entity ent = (Entity) n;
                if(ent.getId()==data.id){
                    n.applyData(data);
                    found=true;
                    needToDestroy[i]=false;
                    break;
                }
            }
            if(found)continue;
            // need to create a new entity
            createEntityFromEntityData(data);
        }

        // destroy entity not founded
        for (int i = 0; i < needToDestroy.length; i++) {
            if(needToDestroy[i]){
                _game.destroyEntity((Entity) _recever.get(i));
            }
        }
    }

    private void createEntityFromEntityData(EntityData entityData){
        Entity ent=switch(entityData.type){
            case PacketDataType.TestNetworkEntity -> {
                TestNetworkEntity.Data d = (TestNetworkEntity.Data)entityData;
                yield new TestNetworkEntity(new Vector2f(d.positionX,d.positionY),d.radius);
            }
            case PacketDataType.Bubble -> new SpaceBubble((SpaceBubble.Data)entityData);
            case PacketDataType.Player -> new ClientPlayer((ServerPlayer.Data)entityData);
            default -> new Entity();
        };

        _serverOwnedEntity.add(_game.createEntity(ent,entityData.id));
    }


    @Override
    public void unregisterNetworkEntity(Entity entity) {
        super.unregisterNetworkEntity(entity);

        _serverOwnedEntity.remove(entity);
    }
}
