package com.metheo.network.server;

import com.metheo.network.packet.PacketType;
import com.metheo.network.packet.PacketUtils;
import com.metheo.network.packet.PingPacket;

import java.io.IOException;
import java.net.*;

public class GameServer extends Thread {
    private DatagramSocket _socket;
    private static final int PORT = 8000;
    private boolean _running=false;

    public GameServer(){
    }

    @Override
    public void run() {
        super.run();
        byte[] buf = new byte[1000];
        DatagramPacket inPkt = new DatagramPacket(buf, buf.length);
        DatagramPacket outPkt = new DatagramPacket(buf, buf.length);
        while (_running){
            try {
                _socket.receive(inPkt);
                System.out.println("Recevied...");
                inPkt = new DatagramPacket(buf, buf.length);
                _socket.receive(inPkt);
                PingPacket result = PacketUtils.unSerialize(buf);
                if(result!=null) {
                    System.out.println("Recevied..." + result.Value);
                    result.Value=true;
                }
                else{
                    result=new PingPacket(false);
                    System.out.println(":[");
                }
                outPkt.setAddress(inPkt.getAddress());
                outPkt.setPort(inPkt.getPort());
                outPkt.setLength(inPkt.getLength());
                outPkt.setData(PacketUtils.serialize(result));
                _socket.send(outPkt);
                System.out.println("Sent...");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return;

    }

    public void start(){
        _running=true;
        try {
            _socket=new DatagramSocket(PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        super.start();
    }

    public boolean isRunning(){
        return _running;
    }


    public void close(){
        _socket.close();
        _running=false;
    }





}
