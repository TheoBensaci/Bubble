/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Interface use to implement function for entity aiming to receve data
 */

package ch.heig.game.core.networkHandler;

import ch.heig.network.packet.data.PacketData;

public interface INetworkReceiverEntity {

    /**
     * Apply pack data to this entity
     * @param data packet data to be apply
     */
    void applyData(PacketData data);
}
