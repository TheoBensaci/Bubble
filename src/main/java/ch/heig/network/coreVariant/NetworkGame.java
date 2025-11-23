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
    protected final NetworkHandlerSystem p_networkHandler;
    protected GameSocket p_gameSocket;

    public NetworkGame(boolean createWindow, String title, NetworkHandlerSystem networkHandler) {
        super(createWindow, title);
        p_networkHandler =networkHandler;
        p_networkHandler.setGame(this);
    }

    @Override
    public void close() {
        super.close();
        if(p_gameSocket !=null){
            p_gameSocket.close();
        }
    }

    @Override
    public void preUpdate() {
        super.preUpdate();

        // feetch socket data
        if(p_gameSocket !=null){
            p_networkHandler.receiveUpdate(p_gameSocket);
        }
    }

    @Override
    public void postUpdate() {
        super.postUpdate();
        // send update data
        if(p_gameSocket !=null){
            p_networkHandler.senderUpdate(p_gameSocket);
        }
    }

    @Override
    protected void registerEntity(Entity ent) {
        super.registerEntity(ent);
        p_networkHandler.registerNetworkEntity(ent);
    }


    @Override
    protected void unregisterEntity(Entity ent) {
        super.unregisterEntity(ent);
        p_networkHandler.unregisterNetworkEntity(ent);
    }


    //#region network

    public void setGameSocket(GameSocket gameSocket){
        p_gameSocket =gameSocket;
        p_gameSocket.start();
    }

    public GameSocket getGameSocket(){
        return p_gameSocket;
    }

    //#endregion
}
