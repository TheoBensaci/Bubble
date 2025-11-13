/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: packet use to send game state
 */

package ch.heig.network.packet;

import java.util.Arrays;

import ch.heig.network.packet.data.PacketData;

public class GameStatePacket extends Packet{
    public static final int PACKET_MAX_SIZE=3000;
    public final PacketData[] data;

    public GameStatePacket(PacketData ... data){
        this.data= Arrays.copyOf(data,data.length);
    }

}
