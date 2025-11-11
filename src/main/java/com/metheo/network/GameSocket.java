package com.metheo.network;

import com.metheo.game.core.render.GameRender;
import com.metheo.network.packet.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GameSocket extends Thread {
    public final boolean isServer;
    private DatagramSocket _socket;
    private static final int PORT = 8000;
    private static final int CLIENT_PORT = 8001;
    private boolean _running=false;

    // server data
    public List<InetAddress> clientsConnected;


    // client data
    private String _hostname;
    private InetAddress _addr;
    private boolean _isConnected=false;
    private ClientSearchThread _cst;

    public final List<Packet> receivedPackets=new ArrayList<>();
    public final Semaphore mutex=new Semaphore(1);


    public GameSocket(){
        isServer=true;
        clientsConnected=new ArrayList<>();
    }

    public GameSocket(String hostName) {
        isServer=false;
        _hostname=hostName;
        try {
            _addr= InetAddress.getByName(_hostname);
        }  catch (UnknownHostException e) {
            _addr=null;
        }

        /*
        _cst=new ClientSearchThread(new JoinPacket());
        _cst.start();*/
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

                mutex.acquire();
                switch (p.type){
                    case PacketType.gameState:

                        break;

                    case PacketType.joiningGameValidation:

                        break;

                    case PacketType.playerInput:
                        if(isServer){
                            if(clientsConnected.contains(inPkt.getAddress())){
                                clientsConnected.add(inPkt.getAddress());
                            }
                            receivedPackets.add(p);
                        }
                        break;


                    default :
                        break;

                }
                mutex.release();

                // If sever and if packet = join game -> register the target


                // else packet -> receve packet


            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void start(){
        _running=true;
        try {
            _socket=new DatagramSocket((isServer)?PORT:CLIENT_PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        super.start();

        if(isServer){
            System.out.println("Sever start");
        }
    }

    public boolean isRunning(){
        return _running;
    }


    public void close(){
        _socket.close();
        _running=false;
    }

    public <E extends Packet> void send(E packet) {
        send(packet,_addr,PORT);
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
