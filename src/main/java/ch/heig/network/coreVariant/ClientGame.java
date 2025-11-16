/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: client variant of the game
 */

package ch.heig.network.coreVariant;

import ch.heig.network.networkHandler.ClientNetworkHandlerSystem;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.ClientPlayer;
import ch.heig.network.packet.LoginPacket;

public class ClientGame extends NetworkGame {
    public final String username;
    public ClientPlayer player;
    public ClientGame(LoginPacket loginPacket, String title) {
        super(true, title,new ClientNetworkHandlerSystem());
        this.username=loginPacket.username;
        this.player = (ClientPlayer) createEntity(new ClientPlayer(1,new Vector2f(0,0),true,this.username),loginPacket.id);

    }

    public ClientGame(LoginPacket loginPacket) {
        this(loginPacket, "Bubble - Client");
    }

    @Override
    public boolean isServer() {
        return false;
    }
}
