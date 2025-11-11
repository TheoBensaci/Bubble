package com.metheo.game.core.networkHandler;

import com.metheo.game.core.Entity;
import com.metheo.game.core.Game;
import com.metheo.game.entity.ClientPlayer;
import com.metheo.network.GameSocket;
import com.metheo.network.packet.Packet;
import com.metheo.network.packet.PacketType;
import com.metheo.network.packet.SimpleDataPacket;

import java.util.ArrayList;

public class NetworkHandlerSystem {
    private final Game _game;
    private ArrayList<INetworkReceverEntity> _recever=new ArrayList<>();
    private ArrayList<INetworkSenderEntity> _sender=new ArrayList<>();

    private long _lastUpdateSend;
    private static long _UPDATE_SEND_FREQUENCY=30000000;        // ~ 30 time pare sec

    public NetworkHandlerSystem(Game game){
        this._game=game;
        _lastUpdateSend=System.nanoTime();
    }


    public void receveUpdate(GameSocket socket){
        if(_game.isServer()){
            if(socket.receivedPackets.isEmpty())return;
            Packet[] buffer=new Packet[0];
            try {
                socket.mutex.acquire();
                buffer = new Packet[socket.receivedPackets.size()];
                socket.receivedPackets.toArray(buffer);
                socket.receivedPackets.clear();
                socket.mutex.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for ( Packet p : buffer){
                switch(p.type){
                    case PacketType.playerInput :
                        /*
                        InputData data = (InputData)((SimpleDataPacket) p).data;

                        boolean found = false;
                        // try to find player in this
                        for(INetworkReceverEntity r : _recever){
                            if(r instanceof NetworkSenderPlayer player){
                                found=data.username.equals(player.username);
                                if(found){
                                    player.applyData(data);
                                    break;
                                }
                            }
                        }

                        if(found)break;

                        // if no player found, create one
                        _game.forceCreate(new NetworkSenderPlayer(data.username,2,new Vector2f(0,0)));
                        */

                    break;
                }
            }

        }
        else {

        }
    }


    public void senderUpdate(GameSocket socket){
        if(System.nanoTime() - _lastUpdateSend < _UPDATE_SEND_FREQUENCY)return;

        _lastUpdateSend=System.nanoTime();
        if(_game.isServer()){

        }
        else {
            for (INetworkSenderEntity r : _sender) {
                if (r instanceof ClientPlayer player) {
                    socket.send(new SimpleDataPacket(PacketType.playerInput, player.getData()));
                }
            }
        }
    }

    public void registerNetworkEntity(Entity entity){
        if(entity instanceof INetworkReceverEntity){
            _recever.add((INetworkReceverEntity) entity);
        }

        if(entity instanceof INetworkSenderEntity){
            _sender.add((INetworkSenderEntity) entity);
        }
    }

    public void unregisterNetworkEntity(Entity entity){
        if(entity instanceof INetworkReceverEntity){
            _recever.remove((INetworkReceverEntity) entity);
        }

        if(entity instanceof INetworkSenderEntity){
            _sender.remove((INetworkSenderEntity) entity);
        }
    }
}
