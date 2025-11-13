/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Interface use to implement function for entity aiming to send data
 */

package ch.heig.game.core.networkHandler;

import ch.heig.network.packet.data.PacketData;

public interface INetworkSenderEntity {

    /**
     * Generate a packet data from this entity data
     * @return
     */
    PacketData getData();
}
