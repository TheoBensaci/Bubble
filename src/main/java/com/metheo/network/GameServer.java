package com.metheo.network;

import java.io.IOException;
import java.net.ServerSocket;

public class GameServer extends Thread {
    private ServerSocket _socket;
    private static final int PORT = 8000;
    private boolean _running=false;

    public GameServer(){
    }

    @Override
    public void run() {
        super.run();
        try {
            while (_running){
                new ServerClientConnectionThread(_socket.accept(),this).start();
            }
        } catch (IOException ignored) {}
    }

    public void start(){
        _running=true;
        try {
            _socket=new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
            System.exit(-1);
        }
        System.out.println("Server start on port : "+PORT);
        super.start();
    }

    public boolean isRunning(){
        return _running;
    }


    public void close(){
        try {
            _socket.close();
        } catch (IOException e) {
            System.exit(-1);
        }
        _running=false;
    }





}
