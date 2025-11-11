package com.metheo.game.coreVariant;

import com.metheo.game.core.Entity;
import com.metheo.game.core.Game;
import com.metheo.game.core.networkHandler.NetworkHandlerSystem;
import com.metheo.network.GameSocket;

public class NetworkGame extends Game {

    // networking
    private final NetworkHandlerSystem _networkHandler;
    private GameSocket _gameSocket;

    public NetworkGame(boolean createWindow, String title) {
        super(createWindow, title);
        _networkHandler=new NetworkHandlerSystem(this);
    }

    @Override
    public void close() {
        super.close();
        if(_gameSocket!=null){
            _gameSocket.close();
        }
    }

    @Override
    public void preUpdate() {
        super.preUpdate();

        // feetch socket data
        if(_gameSocket!=null){
            _networkHandler.receveUpdate(_gameSocket);
        }
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        // send update data
        if(_gameSocket!=null){
            _networkHandler.senderUpdate(_gameSocket);
        }
    }

    @Override
    protected void registerEntity(Entity ent) {
        super.registerEntity(ent);
        _networkHandler.registerNetworkEntity(ent);
    }


    @Override
    protected void unregisterEntity(Entity ent) {
        super.unregisterEntity(ent);
        _networkHandler.registerNetworkEntity(ent);
    }


    //#region network

    public void setGameSocket(GameSocket gameSocket){
        _gameSocket=gameSocket;
        _gameSocket.start();
    }

    public GameSocket getGameSocket(){
        return _gameSocket;
    }

    //#endregion
}
