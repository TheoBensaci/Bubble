package com.metheo.game.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkHandler {
    private Socket _socket;
    private String _hostName;
    private int _port;


    public NetworkHandler(String hostName, int port){
        _port=port;
        _hostName=hostName;
    }

    public String connect(){
        String returnValue="";
        try{
            Socket socket=new Socket(_hostName,_port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while((returnValue = in.readLine()) == null);
            out.println("test");
            while((returnValue = in.readLine()) == null);
            System.out.println(returnValue);


            socket.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return returnValue;
    }
}
