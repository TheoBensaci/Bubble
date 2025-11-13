/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Player use by the server in the server
 */

package ch.heig.game.entity;

import java.awt.Graphics;
import java.util.LinkedList;

import ch.heig.game.core.networkHandler.INetworkSenderEntity;
import ch.heig.game.core.utils.DebugUtils;
import ch.heig.game.core.utils.Vector2f;
import ch.heig.network.packet.data.InputData;
import ch.heig.network.packet.data.PacketData;

public class ServerPlayer extends Player implements INetworkSenderEntity {

    public final String username;

    public LinkedList<InputData> inputDataHistroy=new LinkedList<>();

    private static final float _MAX_SYNC_DISTANCE=5f;
    private int _lastInputNumber=-1;
    private long _lastInputTime;
    private long t;
    private int _currentInput=0;


    public ServerPlayer(String username, int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
        this.username=username;
        _lastInputTime=0;
    }


    @Override
    protected void inputUpdate(float delta) {
         t = System.nanoTime() - _lastInputTime;

        if(inputDataHistroy.isEmpty()){
            //applyInput(new InputData());
            return;
        }
        if(t>=inputDataHistroy.getFirst().delatTimeStart){
            applyNextInput();
        }
    }

    @Override
    protected void stateUpdate(float deltaTime) {
        super.stateUpdate(deltaTime);
        _inSpaceBubble=true;
    }

    public void receiveInput(InputData[] inputDatas){
        if(inputDataHistroy.isEmpty()){
            _lastInputNumber=inputDatas[0].number-1;
        }

        for (int i = inputDatas.length-1; i >=0 ; i--) {
            InputData id = inputDatas[i];
            if(id.number-1 != _lastInputNumber && !(id.number==Integer.MIN_VALUE && _lastInputNumber==Integer.MAX_VALUE))continue;
            if(inputDataHistroy.isEmpty()){
                id.delatTimeStart=0;
            }
            addInput(id);
        }

    }

    private void addInput(InputData inputData){
        inputDataHistroy.addLast(inputData);
        _lastInputNumber=inputData.number;
    }

    private void applyNextInput(){
        if(inputDataHistroy.isEmpty())return;
        InputData id = inputDataHistroy.getFirst();
        applyInput(id);
        inputDataHistroy.removeFirst();
    }

    private void applyInput(InputData id){
        _targetDir.set(id.targetDirectionX,id.targetDirectionY).normilize();
        _rotation=id.rotation;
        _requestDash=id.dash;
        _currentInput=id.number;
        _lastInputTime=System.nanoTime();
        Vector2f targetPos=new Vector2f(id.positionX,id.positionY);
        Vector2f diff=getPosition().sub(targetPos);
        if(diff.magn()<_MAX_SYNC_DISTANCE){
            _position.set(targetPos);
        }
        else{
            // TODO : need to sync the player
        }
    }


    @Override
    public void draw(Graphics g) {
        super.draw(g);

        String[] debugInfo=new String[]{
                "Username : " + username,
                "Position : " + _position,
                "request dash : " + _requestDash,
                "Input history length : " + inputDataHistroy.size(),
                "Current input : "+_currentInput,
                "T : "+(float)(t)/1000000
        };

        DebugUtils.drawEntityDebugInfo(g,_position.copy(),new Vector2f(0, 50),debugInfo);
    }

    @Override
    public PacketData getData() {
        return null;
    }
}
