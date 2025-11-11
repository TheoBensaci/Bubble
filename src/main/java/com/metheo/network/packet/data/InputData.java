package com.metheo.network.packet.data;

import com.metheo.game.core.utils.Input;
import com.metheo.game.core.utils.Vector2f;

public class InputData extends PacketData{
    public int number;
    public long duration;
    public int targetDirectionX;
    public int targetDirectionY;
    public boolean dash;
    public double rotation;
}
