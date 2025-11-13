/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: packet use to send player input
 */

package ch.heig.network.packet;

import ch.heig.network.packet.data.InputData;

public class InputPacket extends Packet{
    public static int INPUT_HISTORY_LENGTH=5;
    public String username;
    public InputData[] input;

    public InputPacket(){
        type=PacketType.playerInput;
    }

}
