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
import ch.heig.other.Arena;
import ch.heig.network.coreVariant.ServerGame;
import ch.heig.entity.SpaceBubble.SpaceBubble;
import ch.heig.network.socket.GameSocket;


public class ServerMain {
    public static void main(String[] args) {
        ServerCliUtils.startServerStartMessage(GameSocket.PORT);
        ServerGame gameServer = new ServerGame(true);
        gameServer.start();
        gameServer.setGameSocket(new GameSocket());

        // load the map

        gameServer.createEntity(new SpaceBubble(new Vector2f(GameRender.WIDTH/2,GameRender.HEIGHT/2),150));


        // set arenna
        Arena.position.set(GameRender.WIDTH/2,GameRender.HEIGHT/2);
        Arena.radiuse=400f;



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
                if(line==null) break;
                String trimmed = line.trim();
                switch (trimmed){
                    case "exit" :
                        wait=false;
                        // send exit to all players before closing
                        gameServer.broadcastExitToAll();
                        gameServer.close();
                        break;

                    case "players":
                        String[] players = gameServer.listPlayers();
                        if(players.length==0){
                            System.out.println("No player connected.");
                        } else {
                            System.out.println("Connected players ("+players.length+"):");
                            for(String p : players){
                                System.out.println(" - "+p);
                            }
                        }
                        break;

                    default:
                        if(trimmed.startsWith("quick ")){
                            String username = trimmed.substring(6).trim();
                            if(username.isEmpty()){
                                System.out.println("Usage: quick [username]");
                            } else {
                                boolean ok = gameServer.kickPlayer(username);
                                if(!ok){
                                    System.out.println("Player not found: "+username);
                                }
                            }
                        }
                        else if(trimmed.startsWith("game ")){
                            String arg = trimmed.substring(5).trim();
                            if(arg.equalsIgnoreCase("Start")){
                                ServerCliUtils.gameStartMessage();
                            }
                            else if(arg.equalsIgnoreCase("Stop")){
                                ServerCliUtils.gameStopMessage();
                                gameServer.close();
                                wait=false;
                            }
                            else if(arg.equalsIgnoreCase("Score")){
                                String board = gameServer.formatScoreBoard();
                                ServerCliUtils.gameScoreMessage(board);
                            }
                            else{
                                System.out.println("Unknown game subcommand: "+arg+" (use Start|Stop|Score)");
                            }
                        }
                        else{
                            System.out.println("'"+line+"' is not know as a command :[");
                        }
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