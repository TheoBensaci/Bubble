package com.metheo.game.core.networkHandler;

import com.metheo.game.coreVariant.ServerGame;
import com.metheo.network.GameSocket;
import com.metheo.network.packet.InputPacket;
import com.metheo.network.packet.Packet;
import com.metheo.network.packet.PacketType;

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
                        server.createNewPlayer(ip.username);
                        return;
                    }
                    server.serverPlayers.get(ip.username).receiveInput(ip.input);
                    break;
            }
        }
    }

    @Override
    public void senderUpdate(GameSocket socket) {
        if(System.nanoTime() - _lastUpdateSend < _UPDATE_SEND_FREQUENCY)return;

        _lastUpdateSend=System.nanoTime();
    }
}
