package com.metheo.game.core.networkHandler;

import com.metheo.game.core.Entity;
import com.metheo.game.core.Game;
import com.metheo.network.GameSocket;

import java.util.ArrayList;

public abstract class NetworkHandlerSystem {
    protected Game _game;
    protected ArrayList<INetworkReceiverEntity> _recever=new ArrayList<>();
    protected ArrayList<INetworkSenderEntity> _sender=new ArrayList<>();

    protected long _lastUpdateSend;
    protected static long _UPDATE_SEND_FREQUENCY=30000000;        // ~ 30 time pare sec

    public NetworkHandlerSystem(){
        _lastUpdateSend=System.nanoTime();
    }

    public void setGame(Game game){
        _game=game;
    }


    public abstract void receiveUpdate(GameSocket socket);

    public abstract void senderUpdate(GameSocket socket);

    public void registerNetworkEntity(Entity entity){
        if(entity instanceof INetworkReceiverEntity){
            _recever.add((INetworkReceiverEntity) entity);
        }

        if(entity instanceof INetworkSenderEntity){
            _sender.add((INetworkSenderEntity) entity);
        }
    }

    public void unregisterNetworkEntity(Entity entity){
        if(entity instanceof INetworkReceiverEntity){
            _recever.remove((INetworkReceiverEntity) entity);
        }

        if(entity instanceof INetworkSenderEntity){
            _sender.remove((INetworkSenderEntity) entity);
        }
    }
}
