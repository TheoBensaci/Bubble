/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Server network handler
 */

package ch.heig.game.core.networkHandler;

import ch.heig.game.core.Entity;
import ch.heig.game.coreVariant.ServerGame;
import ch.heig.network.ClientData;
import ch.heig.network.GameSocket;
import ch.heig.network.packet.*;
import ch.heig.network.packet.data.EntityData;
import ch.heig.network.packet.data.PacketData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ServerNetworkHandlerSystem extends NetworkHandlerSystem{
    public ServerNetworkHandlerSystem() {
        super();
    }

    @Override
    public void receiveUpdate(GameSocket socket) {
        ServerGame server = (ServerGame)_game;

        Packet[] buffer=new Packet[0];
        try{
            socket.mutex.acquire();
            if(socket.receivedPackets.isEmpty()){
                socket.mutex.release();
                return;
            }

            buffer = new Packet[socket.receivedPackets.size()];
            socket.receivedPackets.toArray(buffer);
            socket.receivedPackets.clear();
            socket.mutex.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for ( Packet p : buffer){
            switch(p.type){
                case PacketType.playerInput :
                    InputPacket ip = (InputPacket) (p);
                    if(!server.serverPlayers.containsKey(ip.username)){
                        break;
                    }
                    server.serverPlayers.get(ip.username).entity.receiveInput(ip.input);
                    break;
                case PacketType.login:
                    LoginPacket lp = (LoginPacket)p;
                    if(server.serverPlayers.containsKey(lp.username)){
                        lp.id=-1;
                        socket.send(lp,p.inetAddress,p.port);
                        break;
                    }

                    Entity e = server.createNewPlayer(lp.username,p.inetAddress,p.port);
                    lp.id=e.getId();
                    socket.send(lp,p.inetAddress,p.port);
                    System.out.println("test");
                    break;
            }
        }
    }

    @Override
    public void senderUpdate(GameSocket socket) {
        if(System.nanoTime() - _lastUpdateSend < _UPDATE_SEND_FREQUENCY)return;
        ServerGame server = (ServerGame)_game;

        // build game state packet
        ArrayList<EntityData> data = new ArrayList<>();
        for (INetworkSenderEntity e : _sender) {
            data.add(e.getData());
        }
        GameStatePacket gsp = new GameStatePacket(data.toArray(new EntityData[0]));

        // send it to all player
        for(Map.Entry<String, ClientData> entry : server.serverPlayers.entrySet()){
            socket.send(gsp,entry.getValue().address,entry.getValue().port);
        }
        _lastUpdateSend=System.nanoTime();
    }
}
