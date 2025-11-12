package com.metheo;


import com.metheo.game.core.Game;
import com.metheo.game.core.utils.Vector2f;
import com.metheo.game.coreVariant.ClientGame;
import com.metheo.game.entity.ClientPlayer;
import com.metheo.network.GameSocket;

import java.util.Random;


public class ClientMain {
    public static void main(String[] args) {
        char [] usernameChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890?.,-_:;<>*/+%&=¦@#°§¬|¢()[]$!".toCharArray();


        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            int index = rand.nextInt(0,usernameChar.length);
            sb.append(usernameChar[index]);
        }

        ClientGame game = new ClientGame(sb.toString());
        game.start();
        game.setGameSocket(new GameSocket("localhost",8000,8001));
        game.createEntity(new ClientPlayer(1,new Vector2f(0,0)));

        //ClientPlayer cp =(ClientPlayer)game.createEntity(new ClientPlayer(sb.toString(),1,new Vector2f(0,0)));
        try {
            game.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Game close, bye bye :]");
    }
}