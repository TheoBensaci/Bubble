package com.metheo.game.entity;

import com.metheo.game.core.utils.Vector2f;
import com.metheo.game.core.networkHandler.INetworkSenderEntity;
import com.metheo.game.coreVariant.ClientGame;
import com.metheo.network.packet.InputPacket;
import com.metheo.network.packet.data.InputData;
import com.metheo.network.packet.data.PacketData;

import java.awt.*;
import java.util.Arrays;

public class ClientPlayer extends Player implements INetworkSenderEntity {
    public Vector2f lastDir;
    public InputData[] inputDataHistroy=new InputData[InputPacket.INPUT_HISTORY_LENGTH];
    private int _inputNumber=0;
    private long _lastInputTime=-1;

    public ClientPlayer(int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
        Arrays.fill(this.inputDataHistroy,new InputData());
        _lastInputTime=System.nanoTime();
    }

    @Override
    protected void inputUpdate(float delta) {
        super.inputUpdate(delta);

        if(lastDir!=null && _targetDir.isEqual(lastDir) && !_requestDash){
            return;
        }
        if(lastDir==null)lastDir=new Vector2f(0,0);


        lastDir.set(_targetDir);

        ((ClientGame)getGame()).getGameSocket().send(createPacket());
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
        ClientGame cp = (ClientGame)getGame();
        g.drawString(cp.username,(int)(_position.x-cp.username.length()*3),(int)(_position.y+25));
        g.drawString("Input : "+inputDataHistroy[0],(int)(_position.x),(int)(_position.y+40));
        g.drawString("Number : "+inputDataHistroy[0].number,(int)(_position.x),(int)(_position.y+60));
    }

    @Override
    public PacketData getData() {
        return createInputSnapshot();
    }


    private InputData createInputSnapshot(){
        InputData id = new InputData();
        id.targetDirectionY=Math.round(_targetDir.y);
        id.targetDirectionX=Math.round(_targetDir.x);
        id.rotation=_rotation;
        id.dash=_requestDash;
        id.delatTimeStart=(System.nanoTime()-_lastInputTime);
        id.number=_inputNumber;
        id.positionX=_position.x;
        id.positionY= _position.y;
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
        createInputSnapshot();
        InputPacket ip = new InputPacket();
        ip.input=Arrays.copyOf(inputDataHistroy,inputDataHistroy.length);
        ip.username=((ClientGame)getGame()).username;
        return ip;
    }

    public long getLastInputTime(){
        return _lastInputTime;
    }
}
