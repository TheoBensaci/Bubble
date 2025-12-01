/**
 *   Autheur: Theo Bensaci
 *   Date: 19:00 23.11.2025
 *   Description: Dummy player, use to test thing on the local build
 */

package ch.heig.entity.player;

import ch.heig.core.utils.Vector2f;

public class DummyPlayer extends Player{
    public DummyPlayer(int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
    }

    @Override
    protected void inputUpdate(float delta) {
        return;
    }
}
