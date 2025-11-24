package ch.heig.entity.player;

import ch.heig.core.utils.Vector2f;

public class DummiePlayer extends Player{
    public DummiePlayer(int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
    }

    @Override
    protected void inputUpdate(float delta) {
        return;
    }
}
