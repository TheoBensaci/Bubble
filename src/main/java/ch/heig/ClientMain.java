/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Main use for the client build
 */

package ch.heig;


import ch.heig.game.coreVariant.ClientGame;
import ch.heig.network.GameSocket;
import ch.heig.network.LoginSocket;
import ch.heig.network.packet.GameStatePacket;
import ch.heig.network.packet.Packet;
import ch.heig.network.packet.PacketType;
import ch.heig.network.packet.LoginPacket;

import java.io.IOException;
import java.net.*;
import java.util.Random;


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
        game.setGameSocket(new GameSocket("localhost",8000,8001));


        try {
            game.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Game close, bye bye :]");
    }
}