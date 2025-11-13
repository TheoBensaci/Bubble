/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Client network handler
 */

package ch.heig.game.core.networkHandler;

import ch.heig.game.entity.ClientPlayer;
import ch.heig.network.GameSocket;

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
