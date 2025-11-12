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
