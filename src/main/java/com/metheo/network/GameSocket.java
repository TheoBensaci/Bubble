package com.metheo.network;

import com.metheo.network.packet.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameSocket extends Thread {
    private DatagramSocket _socket;
    private static final int PORT = 8000;
    private boolean _running=false;

    private String _hostname;
    private InetAddress _addr;
    private final int _listendefaultPort;
    private final int _targetDefaultPort;
    private ClientSearchThread _cst;

    public final List<Packet> receivedPackets=new ArrayList<>();
    public final Semaphore mutex=new Semaphore(1);


    public GameSocket(){
        _listendefaultPort=PORT;
        _targetDefaultPort=PORT;
    }

    public GameSocket(int listenDefaultPort){
        _listendefaultPort=listenDefaultPort;
        _targetDefaultPort=PORT;
    }

    public GameSocket(String hostName,int targetDefaultPort,int listenDefaultPort) {
        _hostname=hostName;
        _targetDefaultPort=targetDefaultPort;
        _listendefaultPort=listenDefaultPort;
        try {
            _addr= InetAddress.getByName(_hostname);
        }  catch (UnknownHostException e) {
            _addr=null;
        }
    }

    /**
     * Do somthing on receve of a packet, if the methods return false, the packet is drop and not add to receivedPackets lst
     * @param packet packet receve
     * @return if the packet is to be keep of not
     */
    public boolean onReceivePacket(Packet packet){
        return true;
    }


    @Override
    public void run() {
        super.run();
        byte[] buf;
        DatagramPacket inPkt;

        try {
            while (_running){
                buf = new byte[GameStatePacket.PACKET_MAX_SIZE];
                inPkt = new DatagramPacket(buf, buf.length);
                _socket.receive(inPkt);

                // decode packet
                Packet p = Packet.unserialize(inPkt.getData());
                if(p==null)continue;

                if(!onReceivePacket(p))continue;

                mutex.acquire();
                receivedPackets.add(p);
                mutex.release();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
        }
    }

    public void start(){
        _running=true;
        try {
            _socket=new DatagramSocket(_listendefaultPort);
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

    public <E extends Packet> void send(E packet) {
        send(packet,_addr,_targetDefaultPort);
    }

    private <E extends Packet> void send(E packet,InetAddress inetAddress, int port){
        byte[] buf = new byte[1000];
        try {
            DatagramPacket datagram = new DatagramPacket(buf, buf.length, inetAddress, port);
            datagram.setData(packet.serialize());
            datagram.setPort(port);
            datagram.setAddress(inetAddress);
            _socket.send(datagram);
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
    }


    public class ClientSearchThread extends Thread{
        private boolean _isRunning=true;
        public Packet toSendPacket;
        public ClientSearchThread(Packet packet){
            toSendPacket=packet;
        }

        @Override
        public void run() {
            super.run();
            byte[] buf;
            DatagramPacket datagramPacket;
            try {
                while (_isRunning && _addr!=null) {
                    buf = new byte[1000];
                    datagramPacket = new DatagramPacket(buf, buf.length);
                    datagramPacket.setData(toSendPacket.serialize());
                    datagramPacket.setAddress(_addr);
                    datagramPacket.setPort(PORT);
                    _socket.send(datagramPacket);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void close(){
            _isRunning=false;
        }
    }
}
