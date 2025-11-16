/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: General socket of the game
 */

package ch.heig.network.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import ch.heig.network.packet.GameStatePacket;
import ch.heig.network.packet.Packet;

public class GameSocket extends Thread {
    private DatagramSocket _socket;
    public static final int PORT = 8000;
    private boolean _running=false;

    private String _hostname;
    private InetAddress _addr;
    private final int _listenPort;
    private final int _targetPort;

    public final List<Packet> receivedPackets=new ArrayList<>();
    public final Semaphore mutex=new Semaphore(1);


    public GameSocket(){
        _listenPort =PORT;
        _targetPort =PORT;
    }

    public GameSocket(int listenDefaultPort){
        _listenPort =listenDefaultPort;
        _targetPort =PORT;
    }

    public GameSocket(String hostName,int targetDefaultPort,int listenDefaultPort) {
        _hostname=hostName;
        _targetPort =targetDefaultPort;
        _listenPort =listenDefaultPort;
        try {
            _addr= InetAddress.getByName(_hostname);
        }  catch (UnknownHostException e) {
            System.out.println("UNKOW ADDRESS");
            _addr=null;
        }
    }

    /**
     * Do somthing on receve of a packet, if the methods return false, the packet is drop and not add to receivedPackets lst
     * @param packet packet receve
     * @return if the packet is to be keep of not
     */
    public boolean onReceivePacket(Packet packet, InetAddress addr, int port){
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

                if(!onReceivePacket(p,inPkt.getAddress(),inPkt.getPort()))continue;
                p.inetAddress=inPkt.getAddress();
                p.port=inPkt.getPort();

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
            _socket=new DatagramSocket(_listenPort);
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
        send(packet,_addr, _targetPort);
    }

    public <E extends Packet> void send(E packet,InetAddress inetAddress, int port){
        byte[] buf = new byte[GameStatePacket.PACKET_MAX_SIZE];
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

    public int getListenPort(){
        return _listenPort;
    }
    public int getTargetPort(){
        return _targetPort;
    }
}
