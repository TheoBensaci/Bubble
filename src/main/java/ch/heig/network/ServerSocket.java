/**
 *   Autheur: Theo Bensaci
 *   Date: 18:23 13.11.2025
 *   Description: Special socket fot the server game 
 */

package ch.heig.network;

import java.net.InetAddress;

import ch.heig.game.core.Entity;
import ch.heig.game.coreVariant.ServerGame;
import ch.heig.network.packet.LoginPacket;
import ch.heig.network.packet.Packet;
import ch.heig.network.packet.PacketType;

public class ServerSocket extends GameSocket{

    private final ServerGame _serverGame;
    public ServerSocket(ServerGame serverGame){
        _serverGame=serverGame;
    }

    @Override
    public boolean onReceivePacket(Packet packet, InetAddress addr, int port) {

        if(packet.type== PacketType.login){
            LoginPacket lp = (LoginPacket)packet;
            System.out.println("damn");
            if(_serverGame.serverPlayers.containsKey(lp.username)){
                lp.id=-1;
                send(lp,addr,port);
                return false;
            }

            Entity e = _serverGame.createNewPlayer(lp.username,addr,port);
            lp.id=e.getId();
            send(lp,addr,port);
            return false;
        }


        return true;
    }
}
