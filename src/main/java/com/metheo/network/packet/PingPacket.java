package com.metheo.network.packet;

public class PingPacket extends Packet{
    public boolean Value=false;

    public PingPacket(boolean value){
        Type=PacketType.ping;
        Value=value;
    }
}
