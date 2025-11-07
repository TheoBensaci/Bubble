package com.metheo;

import com.metheo.game.core.Game;
import com.metheo.game.core.render.GameRender;
import com.metheo.game.core.utils.Vector2f;
import com.metheo.game.entity.Player;
import com.metheo.game.entity.SpaceBubble;
import com.metheo.network.client.ClientNetworkHandler;

public class ClientMain {
    public static void main(String[] args) {

        Game game = Game.getGame(false,true);
        Player pl = (Player) game.createEntity(new Player(1,new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2)));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2),100));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2+200,GameRender.HEIGHT/2),50));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2-200,GameRender.HEIGHT/2),50));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2+200),50));
        game.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2-200),50));


        /*
        ClientNetworkHandler clientNetworkHandler = new ClientNetworkHandler("localhost",8000);
        clientNetworkHandler.start();

        */





    }
}