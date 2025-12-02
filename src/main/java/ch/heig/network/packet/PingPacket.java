/** Autheur: Theo Bensaci | Date: 15:12 01.12.2025 | Description: */
package ch.heig.network.packet;

public class PingPacket extends Packet {
  public PingPacket() {
    type = PacketType.ping;
  }
}
