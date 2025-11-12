package ch.heig;

import ch.heig.game.core.Game;
import ch.heig.game.core.render.GameRender;
import ch.heig.game.core.utils.Vector2f;
import ch.heig.game.entity.Player;
import ch.heig.game.entity.SpaceBubble;

public class LocalMain {
    public static void main(String[] args) {
        Game game = new Game(true);
        game.start();
        game.createEntity(new Player(2,new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2)));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2),100));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2+200,GameRender.HEIGHT/2),50));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2-200,GameRender.HEIGHT/2),50));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2+200),50));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2-200),50));

        try {
            game.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Game close, bye bye :]");
    }
}
