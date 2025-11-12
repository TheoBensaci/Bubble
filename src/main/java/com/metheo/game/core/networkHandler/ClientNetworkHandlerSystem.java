package com.metheo.game.core.networkHandler;

import com.metheo.game.core.Game;
import com.metheo.game.entity.ClientPlayer;
import com.metheo.network.GameSocket;
import com.metheo.network.packet.Packet;
import com.metheo.network.packet.PacketType;
import com.metheo.network.packet.SimpleDataPacket;

public class ClientNetworkHandlerSystem extends NetworkHandlerSystem{
    public ClientNetworkHandlerSystem() {
        super();
    }

    public void receiveUpdate(GameSocket socket){
    }


    public void senderUpdate(GameSocket socket){
        for (INetworkSenderEntity r : _sender) {
            if (r instanceof ClientPlayer player) {
                if(System.nanoTime()-player.getLastInputTime() < _UPDATE_SEND_FREQUENCY)continue;
                socket.send(player.createPacket());
            }
        }
    }
}
