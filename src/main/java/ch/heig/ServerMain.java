/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Main use for the server build
 */

package ch.heig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ch.heig.cli.ServerCliUtils;
import ch.heig.core.render.GameRender;
import ch.heig.core.utils.Vector2f;
import ch.heig.network.coreVariant.ServerGame;
import ch.heig.entity.SpaceBubble;
import ch.heig.entity.TestNetworkEntity;
import ch.heig.network.socket.GameSocket;


public class ServerMain {
    public static void main(String[] args) {
        ServerCliUtils.startServerStartMessage(GameSocket.PORT);
        ServerGame gameServer = new ServerGame(true);
        gameServer.start();
        gameServer.setGameSocket(new GameSocket());

        // load the map

        gameServer.createEntity(new TestNetworkEntity(new Vector2f(300,300),20));

        gameServer.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2),150));



        // make a quick way to interact with the server

        ServerCliUtils.serverStartMessage(gameServer.getGameSocket().getListenPort());
        ServerCliUtils.serverCommandMessage();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        ServerCliUtils.serverNewLine();
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

                if(wait) ServerCliUtils.serverNewLine();
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ServerCliUtils.serverStatue(gameServer);
    }
}