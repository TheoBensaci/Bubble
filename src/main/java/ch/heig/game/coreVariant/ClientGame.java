/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: client variant of the game
 */

package ch.heig.game.coreVariant;

import ch.heig.game.core.networkHandler.ClientNetworkHandlerSystem;

public class ClientGame extends NetworkGame {
    public final String username;
    public ClientGame(String username,String title) {
        super(true, title,new ClientNetworkHandlerSystem());
        this.username=username;
    }

    public ClientGame(String username) {
        this(username, "Bubble - Client");
    }

    @Override
    public boolean isServer() {
        return false;
    }
}
