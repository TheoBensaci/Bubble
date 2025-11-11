package com.metheo.network;

import com.metheo.network.packet.data.PacketData;

public interface INetworkReceverEntity {

    void applyData(PacketData data);
}
