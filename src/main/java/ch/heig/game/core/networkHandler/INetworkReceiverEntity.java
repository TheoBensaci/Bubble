package ch.heig.game.core.networkHandler;

import ch.heig.network.packet.data.PacketData;

public interface INetworkReceiverEntity {

    void applyData(PacketData data);
}
