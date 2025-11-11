package com.metheo.network.packet;

import com.metheo.network.packet.data.PacketData;

import java.io.Serializable;
import java.util.Arrays;

public class GameStatePacket extends Packet{
    public static final int PACKET_MAX_SIZE=3000;
    public final PacketData[] data;

    public GameStatePacket(PacketData ... data){
        this.data= Arrays.copyOf(data,data.length);
    }

}
