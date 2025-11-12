package com.metheo.network.packet.data;

import com.metheo.game.core.utils.Input;
import com.metheo.game.core.utils.Vector2f;

public class InputData extends PacketData{
    public int number=0;
    public long delatTimeStart=0;
    public int targetDirectionX=0;
    public int targetDirectionY=0;
    public boolean dash=false;
    public double rotation=0;
    public float positionX=0;
    public float positionY=0;
    public boolean resync=false;

    @Override
    public String toString() {
        return "dir : ("+targetDirectionX+", "+targetDirectionY+") | Dash : "+dash+" | Rotation : "+rotation;
    }
}
