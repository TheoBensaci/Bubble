package ch.heig.network.packet;

import ch.heig.network.packet.data.PacketData;

public class SimpleDataPacket extends Packet {
    public PacketData data;

    public SimpleDataPacket(PacketType type, PacketData data){
        this.type=type;
        this.data=data;
    }
}
