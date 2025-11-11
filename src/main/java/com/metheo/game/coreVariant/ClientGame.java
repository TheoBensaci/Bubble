package com.metheo.game.coreVariant;

import com.metheo.game.core.Game;

public class ClientGame extends NetworkGame {
    public ClientGame(boolean createWindow, String title) {
        super(createWindow, title);
    }

    public ClientGame(boolean createWindow) {
        super(createWindow, "Bubble - Client");
    }

    @Override
    public boolean isServer() {
        return false;
    }
}
