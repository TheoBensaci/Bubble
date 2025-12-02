/** Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: packet use to send game state */
package ch.heig.network.packet;

import ch.heig.network.packet.data.EntityData;
import java.util.Arrays;

public class GameStatePacket extends Packet {
  public static final int PACKET_MAX_SIZE = 3000;

  // actual
  public int stateID = 0;
  public final EntityData[] data;

  public GameStatePacket(int stateID, EntityData... data) {
    this.stateID = stateID;
    this.data = Arrays.copyOf(data, data.length);
    this.type = PacketType.gameState;
  }
}
