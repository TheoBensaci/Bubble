/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Main use to test the game with out networking, useful to devlope the "game" aspect of this project
 */

package ch.heig;

import ch.heig.core.Game;
import ch.heig.core.render.GameRender;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.DummiePlayer;
import ch.heig.other.Arena;
import ch.heig.entity.player.Player;
import ch.heig.entity.SpaceBubble.SpaceBubble;
import ch.heig.entity.TestNetworkEntity;

public class LocalMain {
    public static void main(String[] args) {
        Game game = new Game(true);
        game.start();
        game.createEntity(new Player(2,new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2)),0);
        game.createEntity(new DummiePlayer(1,new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2)),0);
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2),100),0);
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2+200,GameRender.HEIGHT/2),50),0);
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2-200,GameRender.HEIGHT/2),50),0);
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2+200),50),0);
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2-200),50),0);
        game.createEntity(new TestNetworkEntity(new Vector2f(GameRender.WIDTH/2+200,GameRender.HEIGHT/2+200),30),0);

        // set arenna
        Arena.position.set((float)GameRender.WIDTH/2,(float)GameRender.HEIGHT/2);
        Arena.radiuse=375f;

        try {
            game.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Game close, bye bye :]");
    }
}
