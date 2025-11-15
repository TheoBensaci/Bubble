/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Client network handler
 */

package ch.heig.game.core.networkHandler;

import ch.heig.game.coreVariant.ClientGame;
import ch.heig.network.GameSocket;

public class ClientNetworkHandlerSystem extends NetworkHandlerSystem{


    public ClientNetworkHandlerSystem() {
        super();
    }

    public void receiveUpdate(GameSocket socket){
    }


    public void senderUpdate(GameSocket socket){
        ClientGame cg = (ClientGame) _game;
        if(cg.player==null)return;
        if(cg.player.getClock() < 50)return;
        socket.send(cg.player.createPacket());
    }



}
