/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: class use to manage network in the game
 */

package ch.heig.network.networkHandler;

import java.util.ArrayList;

import ch.heig.core.Entity;
import ch.heig.core.Game;
import ch.heig.network.socket.GameSocket;

public abstract class NetworkHandlerSystem {
    protected Game _game;
    protected ArrayList<INetworkReceiverEntity> _recever=new ArrayList<>();             // list of entity receving data
    protected ArrayList<INetworkSenderEntity> _sender=new ArrayList<>();                // list of entity sending data

    protected long _lastUpdateSend;
    protected static long _UPDATE_SEND_FREQUENCY=15000000;        // ~ 30 time pare sec

    public NetworkHandlerSystem(){
        _lastUpdateSend=System.nanoTime();
    }

    public void setGame(Game game){
        _game=game;
    }


    /**
     * Do the receve update on every _recever
     * @param socket socket use for this update
     */
    public abstract void receiveUpdate(GameSocket socket);

    /**
     * Do the sender update on every _sender
     * @param socket socket use for this update
     */
    public abstract void senderUpdate(GameSocket socket);

    /**
     * register a new network entity
     * @param entity
     */
    public void registerNetworkEntity(Entity entity){
        if(entity instanceof INetworkReceiverEntity){
            _recever.add((INetworkReceiverEntity) entity);
        }

        if(entity instanceof INetworkSenderEntity){
            _sender.add((INetworkSenderEntity) entity);
        }
    }

    /**
     * unregister a new network entity
     * @param entity
     */
    public void unregisterNetworkEntity(Entity entity){
        if(entity instanceof INetworkReceiverEntity){
            _recever.remove((INetworkReceiverEntity) entity);
        }

        if(entity instanceof INetworkSenderEntity){
            _sender.remove((INetworkSenderEntity) entity);
        }
    }
}
