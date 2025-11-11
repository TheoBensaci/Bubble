package com.metheo.network.packet.data;

import com.metheo.game.core.utils.Input;
import com.metheo.game.core.utils.Vector2f;

public class InputData extends PacketData{
    public float targetDirectionX;
    public float targetDirectionY;
    public boolean dash;
    public double rotation;
    public String username;
    public InputData(String username,Vector2f targetDir, double rotation, boolean dash){
        targetDirectionX=targetDir.x;
        this.rotation=rotation;
        this.username=username;
        this.dash=dash;
        targetDirectionY=targetDir.y;
    }
}
