/** Autheur: Theo Bensaci | Date: 11:49 02.12.2025 | Description: Exit packet */
package ch.heig.network.packet;

public class ExitPacket extends Packet {
  public String username;

  public ExitPacket(String username) {
    this.type = PacketType.exit;
    this.username = username;
  }
}
