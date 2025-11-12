package com.metheo.game.core.networkHandler;

import com.metheo.network.packet.data.PacketData;

public interface INetworkReceiverEntity {

    void applyData(PacketData data);
}
