package com.metheo;

import java.io.*;

import com.metheo.network.packet.PacketUtils;
import com.metheo.network.packet.PingPacket;
import com.metheo.network.server.GameServer;
import com.metheo.network.packet.Packet;


public class ServerMain {
    public static void main(String[] args) {
        System.out.println("Try to Start the Server");
        GameServer server = new GameServer();
        server.start();


        // make a quick way to interact with the server
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        boolean wait = true;
        try {
            while (wait) {
                line = in.readLine();
                switch (line){
                    case "exit" :
                        wait=false;
                        server.close();
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

        System.out.println("Server running -> "+server.isRunning());
    }
}