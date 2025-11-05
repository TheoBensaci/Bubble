package com.metheo;

import com.metheo.game.core.Game;
import com.metheo.game.core.render.GameRender;
import com.metheo.game.core.utils.Vector2f;
import com.metheo.game.entity.Player;
import com.metheo.game.entity.SpaceBubble;

public class Main {
    public static void main(String[] args) {
        Game game = Game.creatGame(true);
        Player pl = new Player(2,new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2));
        new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2),100);
    }
}