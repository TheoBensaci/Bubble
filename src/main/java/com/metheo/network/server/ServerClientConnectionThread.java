package com.metheo.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerClientConnectionThread extends Thread{
    private Socket _socket;
    private GameServer _server;


    public ServerClientConnectionThread(Socket socket, GameServer server){
        super("ServerThread");
        this._socket=socket;
        _server=server;
    }

    @Override
    public void run() {
        try(
                PrintWriter out = new PrintWriter(_socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                ){
            System.out.println("Connection Start");
            String inputLine, outputLine;
            //KnockKnockProtocol kkp = new KnockKnockProtocol();
            //outputLine = kkp.processInput(null);
            out.println();

            while (_server.isRunning()) {
                if((inputLine = in.readLine()) == null)continue;
                System.out.println(inputLine);
                if(inputLine.equals("test")){
                    out.println("OK");
                    break;
                }
                out.println(":[");
            }
            _socket.close();
            System.out.println("Connection end with : "+_socket.getLocalAddress()+" -> THREAD CLOSE");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
