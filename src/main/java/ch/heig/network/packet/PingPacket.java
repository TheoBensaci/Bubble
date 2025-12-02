package ch.heig.network.packet;

public class PingPacket extends Packet{
    public PingPacket(){
        type=PacketType.ping;
    }
}
