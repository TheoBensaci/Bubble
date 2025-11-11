package com.metheo.game.entity;

import com.metheo.game.core.utils.Vector2f;
import com.metheo.network.INetworkSenderEntity;
import com.metheo.network.packet.PacketType;
import com.metheo.network.packet.SimpleDataPacket;
import com.metheo.network.packet.data.InputData;
import com.metheo.network.packet.data.PacketData;

import java.awt.*;

public class ClientPlayer extends Player implements INetworkSenderEntity {
    public String username;
    public Vector2f lastDir;
    public ClientPlayer(String username,int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
        this.username=username;
    }

    @Override
    protected void inputUpdate(float delta) {
        super.inputUpdate(delta);

        if(lastDir!=null && (_tragetDir.x==lastDir.x && _tragetDir.y== lastDir.y) && !_requestDash){
            return;
        }
        if(lastDir==null)lastDir=new Vector2f(0,0);

        lastDir.set(_tragetDir);


        SimpleDataPacket data = new SimpleDataPacket(PacketType.playerInput,getData());

        getGame().getGameSocket().send(data);
    }

    @Override
    protected void stateUpdate(float deltaTime) {
        super.stateUpdate(deltaTime);

        _inSpaceBubble=true;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.setColor(Color.white);
        g.drawString(username,(int)(_position.x-username.length()*3),(int)(_position.y+25));
    }

    @Override
    public PacketData getData() {
        return new InputData(username,_tragetDir,_rotation,_requestDash);
    }
}
