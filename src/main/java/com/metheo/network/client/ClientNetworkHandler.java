package com.metheo.network.client;

import com.metheo.network.packet.PacketUtils;
import com.metheo.network.packet.PingPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ClientNetworkHandler extends Thread{
    private DatagramSocket _socket;
    private String _hostName;
    private int _port;


    public ClientNetworkHandler(String hostName, int port){
        _port=port;
        _hostName=hostName;
    }

    @Override
    public void start() {
        super.start();
        try {
            _socket=new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        System.out.println("(0)");
    }

    @Override
    public void run() {
        super.run();
        byte[] buf = new byte[1000];
        DatagramPacket inPkt = new DatagramPacket(buf, buf.length);
        DatagramPacket outPkt = new DatagramPacket(buf, buf.length);

        PingPacket packet = new PingPacket(false);

        byte[] outBuf = PacketUtils.serialize(packet);
        try {
            System.out.println("(1)");
            outPkt = new DatagramPacket(outBuf, outBuf.length, InetAddress.getByName(_hostName), _port);
            _socket.send(outPkt);
            System.out.println("(2)");

            byte[] inBuf = new byte[1000];
            inPkt = new DatagramPacket(inBuf, inBuf.length);
            _socket.receive(inPkt);
            System.out.println("(3)");
            PingPacket result = PacketUtils.unSerialize(inBuf);
            if(result!=null) {
                System.out.println("Recevied..." + result.Value);
            }
            else{
                System.out.println(":[");
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Sent...");
    }
}
