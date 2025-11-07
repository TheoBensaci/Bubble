package com.metheo.game.entity;

import com.metheo.game.core.utils.Vector2f;

public class ClientPlayer extends NetworkPlayer{
    public ClientPlayer(int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
    }

    @Override
    protected void stateUpdate(float deltaTime) {
        super.stateUpdate(deltaTime);

    }
}
