/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Player use by the client in the client
 */

package ch.heig.entity.player;

import java.awt.*;
import java.util.Arrays;

import ch.heig.network.networkHandler.INetworkReceiverEntity;
import ch.heig.core.utils.Vector2f;
import ch.heig.network.coreVariant.ClientGame;
import ch.heig.network.packet.InputPacket;
import ch.heig.network.packet.data.InputData;
import ch.heig.network.packet.data.PacketData;

public class ClientPlayer extends Player implements INetworkReceiverEntity {
    public Vector2f lastDir;
    private final boolean _mainClient;
    public InputData[] inputDataHistroy=new InputData[InputPacket.INPUT_HISTORY_LENGTH];
    private float _clock=0;
    private int _inputNumber=0;
    private String _username;

    public ClientPlayer(int playerNumber, Vector2f initPosition, boolean mainClient, String username) {
        super(playerNumber, initPosition);
        Arrays.fill(this.inputDataHistroy,new InputData());
        _mainClient=mainClient;
        _username=username;
    }

    public ClientPlayer(ServerPlayer.Data data) {
        super(1, new Vector2f(data.positionX, data.positionY));
        _mainClient=false;
        _username=data.username;
    }

    @Override
    protected void inputUpdate(float delta) {
        if(!_mainClient)return;
        super.inputUpdate(delta);

        _clock+=delta;

        if(lastDir!=null && _targetDir.isEqual(lastDir) && !_requestDash){
            return;
        }
        if(lastDir==null)lastDir=new Vector2f(0,0);


        lastDir.set(_targetDir);

        ((ClientGame)getGame()).getGameSocket().send(createPacket());
    }

    @Override
    public void draw(Graphics g) {
        ClientGame cp = (ClientGame)getGame();
        float usernameSize= cp.username.length()*6.5f;
        Vector2f offset=new Vector2f(20+usernameSize/2,-5);
        Vector2f usernamePos=getPosition().add(offset);
        g.setColor(new Color(0xffff55));
        g.fillRect((int)(usernamePos.x-usernameSize/2),(int)(usernamePos.y-17),(int)(usernameSize),4);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        Vector2f lineStartPose=new Vector2f(new Vector2f((usernamePos.x-usernameSize/2),(int)(usernamePos.y-15)));

        Vector2f diff = getPosition().sub(lineStartPose);

        Vector2f lineEndPos=lineStartPose.copy().add(diff.copy().mult(0.3f));

        g.drawLine((int)(lineStartPose.x),(int)(lineStartPose.y),(int)(lineEndPos.x),(int)(lineEndPos.y));
        g.drawString(_username,(int)(usernamePos.x-usernameSize/2),(int)(usernamePos.y-25));
        super.draw(g);
    }



    private InputData createInputSnapshot(){
        InputData id = new InputData();
        id.targetDirectionY=Math.round(_targetDir.y);
        id.targetDirectionX=Math.round(_targetDir.x);
        id.rotation=_rotation;
        id.dash=_requestDash;
        id.delatTimeStart=_clock;
        id.number=_inputNumber;
        id.positionX=_position.x;
        id.positionY= _position.y;
        _inputNumber++;
        _clock=0;

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

    public float getClock(){
        return _clock;
    }

    @Override
    public void applyData(PacketData data) {
        ServerPlayer.Data d = (ServerPlayer.Data)data;
        _position.set(d.positionX,d.positionY);
        _onDash=d.onDash;
        this._ammo=d.amo;
        this._numberDash=d.numberOfDash;
    }
}
