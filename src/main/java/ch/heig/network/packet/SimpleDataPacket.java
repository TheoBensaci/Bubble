/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Simple packet struct to in bed a single packet type
 */

package ch.heig.network.packet;

import ch.heig.network.packet.data.PacketData;

public class SimpleDataPacket extends Packet {
    public PacketData data;

    public SimpleDataPacket(PacketType type, PacketData data){
        this.type=type;
        this.data=data;
    }
}
