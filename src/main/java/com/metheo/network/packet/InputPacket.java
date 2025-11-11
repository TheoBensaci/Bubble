package com.metheo.network.packet;

import com.metheo.network.packet.data.InputData;

public class InputPacket {
    public static int INPUT_HISTORY_LENGTH=5;
    public String username;
    public InputData[] input;

}
