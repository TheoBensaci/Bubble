package com.metheo.network.packet;

import com.metheo.game.core.utils.Vector2f;
import com.metheo.network.packet.data.InputData;
import com.metheo.network.packet.data.PacketData;
import com.metheo.network.packet.data.PacketDataType;

public class SimpleDataPacket extends Packet {
    public PacketData data;

    public SimpleDataPacket(PacketType type, PacketData data){
        this.type=type;
        this.data=data;
    }
}
