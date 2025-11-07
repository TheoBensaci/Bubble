package com.metheo.game.entity;

import com.metheo.game.core.utils.Vector2f;

public class NetworkPlayer extends Player{
    public NetworkPlayer(int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
    }

    @Override
    protected void stateUpdate(float deltaTime) {
        return;
    }

    @Override
    protected void movementUpdate(float deltaTime) {
        return;
    }

    @Override
    protected void atUpdateEnd(float deltaTime) {
        return;
    }
}
