package com.metheo;

import java.io.*;

import com.metheo.game.coreVariant.ServerGame;
import com.metheo.network.GameSocket;


public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Try to Start the Server");
        ServerGame gameServer = new ServerGame(true);
        gameServer.start();
        GameSocket socket = new GameSocket();
        gameServer.setGameSocket(socket);


        System.out.println("Server socket -> "+(socket.isRunning()?"Running":"Stopped"));


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

        System.out.println("Server socket -> "+(socket.isRunning()?"Running":"Stopped"));
    }
}