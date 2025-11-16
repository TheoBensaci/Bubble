/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: packet use to send game state
 */

package ch.heig.network.packet;

import java.util.Arrays;

import ch.heig.network.packet.data.EntityData;

public class GameStatePacket extends Packet{
    public static final int PACKET_MAX_SIZE=3000;
    public int number=0;
    public final EntityData[] data;

    public GameStatePacket(EntityData ... data){
        this.data= Arrays.copyOf(data,data.length);
        this.type=PacketType.gameState;
    }

}
