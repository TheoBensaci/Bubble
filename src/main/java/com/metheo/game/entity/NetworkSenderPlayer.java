package com.metheo.game.entity;

import com.metheo.game.core.utils.DebugUtils;
import com.metheo.game.core.utils.Vector2f;
import com.metheo.network.INetworkReceverEntity;
import com.metheo.network.packet.data.InputData;
import com.metheo.network.packet.data.PacketData;

import java.awt.*;

public class NetworkSenderPlayer extends Player implements INetworkReceverEntity {

    public final String username;


    public NetworkSenderPlayer(String username,int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
        this.username=username;
    }

    @Override
    protected void inputUpdate(float delta) {
        return;
    }

    @Override
    protected void stateUpdate(float deltaTime) {
        super.stateUpdate(deltaTime);
        _inSpaceBubble=true;
    }

    @Override
    public void applyData(PacketData data) {
        if(data instanceof InputData id) {
            if(!id.username.equals(username))return;
            _tragetDir.set(id.targetDirectionX, id.targetDirectionY);
            _rotation = id.rotation;
            _requestDash = id.dash;

        /*
        PlayerData pd = (PlayerData) data;
        setPosition(pd.positionX,pd.positionY);
        _rotation=pd.rotation;
        _ammo=pd.amo;

        _numberDash=pd.numberOfDash;*/
        }
    }

    /*
    @Override
    public PacketData getData() {
        return new PlayerData(
                _position.x,
                _position.y,
                _rotation,
                getId(),
                true,
                _onDash,
                _ammo,
                _numberDash
        );
    }*/


    @Override
    public void draw(Graphics g) {
        super.draw(g);

        String[] debugInfo=new String[]{
                "Username : " + username,
                " request dash : "+_requestDash
        };

        DebugUtils.drawEntityDebugInfo(g,_position.copy(),new Vector2f(0, 50),debugInfo);
    }
}
