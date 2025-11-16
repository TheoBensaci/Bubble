/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Game implemeting network handling
 */

package ch.heig.network.coreVariant;

import ch.heig.core.Entity;
import ch.heig.core.Game;
import ch.heig.network.networkHandler.NetworkHandlerSystem;
import ch.heig.network.socket.GameSocket;

public class NetworkGame extends Game {

    // networking
    protected final NetworkHandlerSystem _networkHandler;
    protected GameSocket _gameSocket;

    public NetworkGame(boolean createWindow, String title, NetworkHandlerSystem networkHandler) {
        super(createWindow, title);
        _networkHandler=networkHandler;
        _networkHandler.setGame(this);
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
            _networkHandler.receiveUpdate(_gameSocket);
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
        _networkHandler.unregisterNetworkEntity(ent);
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
