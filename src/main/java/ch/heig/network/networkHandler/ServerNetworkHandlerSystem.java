/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Server network handler
 */

package ch.heig.network.networkHandler;

import ch.heig.core.Entity;
import ch.heig.network.coreVariant.ServerGame;
import ch.heig.network.ClientData;
import ch.heig.network.socket.GameSocket;
import ch.heig.network.packet.*;
import ch.heig.network.packet.data.EntityData;

import java.util.*;

public class ServerNetworkHandlerSystem extends NetworkHandlerSystem{
    private int _gameStateNumber=0;

    private static float _TIME_BEFOR_LOGOUT=500;


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
                    ClientData cd = server.serverPlayers.get(ip.username);
                    cd.entity.receiveInput(ip.input);
                    server.serverPlayers.get(ip.username).lastUpdateClock=0;
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
        gsp.number=_gameStateNumber;
        _gameStateNumber++;

        // send it to all player
        Set<Map.Entry<String,ClientData>> set = server.serverPlayers.entrySet();
        boolean[] needToBeLogout=new boolean[set.size()];
        Arrays.fill(needToBeLogout,true);

        Iterator it = set.iterator();

        for (int i = 0; i < needToBeLogout.length; i++) {
            Map.Entry<String,ClientData> cd = (Map.Entry<String,ClientData>)it.next();
            if(cd.getValue().lastUpdateClock<_TIME_BEFOR_LOGOUT){
                socket.send(gsp,cd.getValue().address,cd.getValue().port);
                needToBeLogout[i]=false;
                cd.getValue().lastUpdateClock+=server.getDeltaTime();
            }
        }

        it = set.iterator();
        for (int i = 0; i < needToBeLogout.length; i++) {
            Map.Entry<String,ClientData> cd = (Map.Entry<String,ClientData>)it.next();
            if(needToBeLogout[i]){
                server.destroyPlayer(cd.getKey());
            }
        }

        _lastUpdateSend=System.nanoTime();
    }
}
