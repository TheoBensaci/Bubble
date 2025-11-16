/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Main use for the client build
 */

package ch.heig;


import ch.heig.network.coreVariant.ClientGame;
import ch.heig.network.socket.GameSocket;
import ch.heig.network.socket.LoginSocket;

import java.net.*;


public class ClientMain {
    public static void main(String[] args) {

        InetAddress hostName ;

        try{
            hostName = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        int port=8001;
        int hostPort = 8000;

        LoginSocket loginSocket=new LoginSocket(hostName,hostPort,port);

        ClientGame game = new ClientGame(loginSocket.login());
        game.start();
        game.setGameSocket(new GameSocket("localhost",8000,port));


        port=7999;
        hostPort = 8000;

        loginSocket=new LoginSocket(hostName,hostPort,port);

        ClientGame game2 = new ClientGame(loginSocket.login());
        game2.start();
        game2.setGameSocket(new GameSocket("localhost",8000,port));


        try {
            game.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Game close, bye bye :]");
    }
}