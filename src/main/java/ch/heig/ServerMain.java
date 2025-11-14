/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Main use for the server build
 */

package ch.heig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.heig.game.core.Game;
import ch.heig.game.core.utils.Vector2f;
import ch.heig.game.coreVariant.ServerGame;
import ch.heig.game.entity.TestNetworkEntity;
import ch.heig.network.GameSocket;


public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Try to Start the Server");
        ServerGame gameServer = new ServerGame(true);
        gameServer.start();
        gameServer.setGameSocket(new GameSocket());

        gameServer.createEntity(new TestNetworkEntity(new Vector2f(300,300),20));


        // make a quick way to interact with the server
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        boolean wait = true;
        try {
            while (wait && gameServer.isRunning()) {
                line = in.readLine();
                switch (line){
                    case "exit" :
                        wait=false;
                        gameServer.close();
                        break;

                    default:
                        System.out.println("'"+line+"' is not know as a command :[");
                        break;
                }
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server socket -> "+(gameServer.isRunning()?"Running":"Stopped"));
    }
}