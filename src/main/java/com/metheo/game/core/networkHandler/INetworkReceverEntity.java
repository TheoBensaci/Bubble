package com.metheo.game.core.networkHandler;

import com.metheo.network.packet.data.PacketData;

public interface INetworkReceverEntity {

    void applyData(PacketData data);
}
