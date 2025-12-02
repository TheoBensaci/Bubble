/** Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: General packet data class */
package ch.heig.network.packet.data;

import java.io.Serializable;

public class PacketData implements Serializable {
  public PacketDataType type;

  /***
   * safe cast a packet data to a class E and handle miss cast
   * @return casted object
   */
  public <T extends PacketData> T safeCast(Class<T> clazz) {
    T buf = clazz != null && clazz.isInstance(this) ? clazz.cast(this) : null;
    if (buf == null) {
      System.err.println(
          "ERROR : mist cast of packet data to " + ((clazz == null) ? "NULL" : clazz.getName()));
    }
    return buf;
  }
}
