package com.metheo.game.entity;

import com.metheo.game.core.utils.Vector2f;
import com.metheo.game.core.networkHandler.INetworkSenderEntity;
import com.metheo.game.coreVariant.ClientGame;
import com.metheo.network.packet.InputPacket;
import com.metheo.network.packet.PacketType;
import com.metheo.network.packet.SimpleDataPacket;
import com.metheo.network.packet.data.InputData;
import com.metheo.network.packet.data.PacketData;

import java.awt.*;
import java.util.Arrays;

public class ClientPlayer extends Player implements INetworkSenderEntity {
    public String username;
    public Vector2f lastDir;
    public InputData[] inputDataHistroy=new InputData[InputPacket.INPUT_HISTORY_LENGTH];
    private int _inputNumber=0;
    private long _lastInputTime;

    public ClientPlayer(String username,int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
        this.username=username;
        Arrays.fill(this.inputDataHistroy,new InputData());
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

        ((ClientGame)getGame()).getGameSocket().send(data);
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
        InputData id = new InputData();
        id.targetDirectionY=Math.round(_tragetDir.y);
        id.targetDirectionX=Math.round(_tragetDir.x);
        id.rotation=_rotation;
        id.dash=_requestDash;
        id.duration=(System.nanoTime()-_lastInputTime);
        id.number=_inputNumber;
        _inputNumber++;
        _lastInputTime=System.nanoTime();

        // add input data to the history
        addInputDataToHistory(id);

        return id;
    }


    private void addInputDataToHistory(InputData data){
        InputData buffer,buffer2;
        buffer=inputDataHistroy[0];
        for (int i = 1; i < inputDataHistroy.length; i++) {
            buffer2=inputDataHistroy[i];
            inputDataHistroy[i]=buffer;
            buffer=buffer2;
        }
        inputDataHistroy[0]=data;
    }


    public InputPacket createPacket(){
        InputPacket ip = new InputPacket();
        ip.username=username;
        ip.input=Arrays.copyOf(inputDataHistroy,inputDataHistroy.length);
        return ip;
    }
}
