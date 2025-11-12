package ch.heig.network.packet;

import ch.heig.network.packet.data.PacketData;

import java.util.Arrays;

public class GameStatePacket extends Packet{
    public static final int PACKET_MAX_SIZE=3000;
    public final PacketData[] data;

    public GameStatePacket(PacketData ... data){
        this.data= Arrays.copyOf(data,data.length);
    }

}
