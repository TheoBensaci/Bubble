/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Player use by the server in the server
 */

package ch.heig.entity.player;

import java.awt.Graphics;
import java.util.LinkedList;

import ch.heig.network.networkHandler.INetworkReceiverEntity;
import ch.heig.network.networkHandler.INetworkSenderEntity;
import ch.heig.core.utils.DebugUtils;
import ch.heig.core.utils.Vector2f;
import ch.heig.network.packet.data.*;

public class ServerPlayer extends Player implements INetworkSenderEntity, INetworkReceiverEntity {

    public final String username;

    public LinkedList<InputData> inputDataHistroy=new LinkedList<>();

    private static final float _MAX_SYNC_DISTANCE=5f;
    private static final int _MAX_INPUT_STACK=6;
    private int _lastInputNumber=-1;
    private long _lastInputTime;
    private long t;
    private int _currentInput=0;
    private float _clock=0;


    public ServerPlayer(String username, int playerNumber, Vector2f initPosition) {
        super(playerNumber, initPosition);
        this.username=username;
        _lastInputTime=0;
    }

    public ServerPlayer(ServerPlayer.Data data) {
        this(data.username,1,new Vector2f(data.positionX,data.positionY));
    }


    @Override
    protected void inputUpdate(float delta) {
        if(!getGame().isServer())return;
        _clock+=delta;
         t = System.nanoTime() - _lastInputTime;

        if(inputDataHistroy.isEmpty()){
            //applyInput(new InputData());
            return;
        }
        if(_clock>=inputDataHistroy.getFirst().delatTimeStart){
            applyNextInput();
        }
    }


    public void receiveInput(InputData[] inputDatas){
        if(inputDataHistroy.isEmpty()){
            _lastInputNumber=inputDatas[0].number-1;
            inputDatas[0].delatTimeStart=0;
        }


        for (int i = inputDatas.length-1; i >=0 ; i--) {
            InputData id = inputDatas[i];
            if(id.number-1 != _lastInputNumber && !(id.number==Integer.MIN_VALUE && _lastInputNumber==Integer.MAX_VALUE))continue;
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
        _clock=0;
        Vector2f targetPos=new Vector2f(id.positionX,id.positionY);
        Vector2f diff=getPosition().sub(targetPos);

        // TODO : Client side prediction
        if(diff.magn()<_MAX_SYNC_DISTANCE){
            //_position.set(targetPos);
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
    public void applyData(PacketData data) {
        ServerPlayer.Data d = (ServerPlayer.Data)data;
        _position.set(d.positionX,d.positionY);
        _onDash=d.onDash;
        this._ammo=d.amo;
        this._numberDash=d.numberOfDash;
        this._rotation=d.rotation;
    }

    public static class Data extends CollisionBodyData {
        public double rotation;
        public boolean isAlive;
        public boolean onDash;
        public int amo;
        public int numberOfDash;
        public String username;

        public Data(ServerPlayer ent) {
            super(ent);
            type = PacketDataType.Player;
            this.rotation = ent._rotation;
            this.isAlive = true;
            this.onDash = ent._onDash;
            this.amo = ent._ammo;
            this.numberOfDash = ent._numberDash;
            this.username=ent.username;
            this.rotation=ent._rotation;
        }
    }

    public EntityData getData() {
        return new Data(this);
    }
}
