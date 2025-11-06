package com.metheo;

import com.metheo.network.Server;
import java.io.*;


public class ServerMain {
    public static void main(String[] args) {


        System.out.println("Try to Start the Server");
        Server server = new Server();
        server.start();

        boolean wait = true;
        while (wait){
            String u=System.console().readLine();
            switch (u){
                case "exit" :
                    wait=false;
                    server.close();
                    break;

                default:
                    System.out.println("'"+u+"' is not know as a command :[");
                    break;
            }
        }

        System.out.println("Server running -> "+server.isRunning());
    }
}