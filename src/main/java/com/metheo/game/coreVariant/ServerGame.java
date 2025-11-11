package com.metheo.game.coreVariant;

public class ServerGame extends NetworkGame {
    public ServerGame(boolean createWindow, String title) {
        super(createWindow, title);
    }

    public ServerGame(boolean createWindow) {
        super(createWindow, "Bubble - Server");
    }

    @Override
    public boolean isServer() {
        return true;
    }
}
