/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Player use by the server in the server
 */

package ch.heig.entity.player;

import java.awt.Graphics;
import java.util.LinkedList;

import ch.heig.core.Entity;
import ch.heig.entity.SpaceBubble.SpaceBubble;
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

        // Faille safe for the bug i, apparently, can't resolve :[
        if(inputDataHistroy.size()>inputDatas.length){
            inputDataHistroy.clear();
        }

        boolean found = false;
        for (int i = inputDatas.length-1; i >=0 ; i--) {
            InputData id = inputDatas[i];
            if(id.number-1 != _lastInputNumber && !(id.number==Integer.MIN_VALUE && _lastInputNumber==Integer.MAX_VALUE))continue;
            addInput(id);
            found=true;
        }

        if(!found){
            // TODO : resync
            inputDataHistroy.clear();
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
        p_targetDir.set(id.targetDirectionX,id.targetDirectionY).normilize();
        p_rotation =id.rotation;
        p_requestDash =id.dash;
        p_requestShoot=id.shoot;
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
                "Position : " + p_position,
                "request dash : " + p_requestDash,
                "Input history length : " + inputDataHistroy.size(),
                "Current input : "+_currentInput,
                "T : "+(float)(t)/1000000
        };

        DebugUtils.drawEntityDebugInfo(g, p_position.copy(),new Vector2f(0, 50),debugInfo);
    }

    @Override
    public void applyData(PacketData data) {
        return;
    }

    public static class Data extends CollisionBodyData {
        public double rotation;
        public boolean isAlive;
        public boolean onDash;
        public int amo;
        public int numberOfDash;
        public String username;
        public int actualBubbleId=0;

        public Data(ServerPlayer ent) {
            super(ent);
            type = PacketDataType.Player;
            this.isAlive = true;
            this.onDash = ent.p_onDash;
            this.amo = ent.p_ammo;
            this.numberOfDash = ent.p_numberDash;
            this.username=ent.username;
            this.rotation=ent.p_rotation;
            this.actualBubbleId=(ent.p_actualBubble!=null)?ent.p_actualBubble.getId():0;
        }
    }

    public EntityData getData() {
        return new Data(this);
    }
}
