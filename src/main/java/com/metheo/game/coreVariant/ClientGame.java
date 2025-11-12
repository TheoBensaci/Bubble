package com.metheo.game.coreVariant;

import com.metheo.game.core.Game;
import com.metheo.game.core.networkHandler.ClientNetworkHandlerSystem;
import com.metheo.game.core.networkHandler.NetworkHandlerSystem;

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
