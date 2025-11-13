/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Server variant of the game
 */

package ch.heig.game.coreVariant;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import ch.heig.game.core.Entity;
import ch.heig.game.core.networkHandler.ServerNetworkHandlerSystem;
import ch.heig.game.core.utils.Vector2f;
import ch.heig.game.entity.ServerPlayer;
import ch.heig.network.ClientData;

public class ServerGame extends NetworkGame {

    public final Map<String, ClientData> serverPlayers=new HashMap<>();

    public ServerGame(boolean createWindow, String title) {
        super(createWindow, title, new ServerNetworkHandlerSystem());
    }

    public ServerGame(boolean createWindow) {
        this(createWindow, "Bubble - Server");
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public Entity createNewPlayer(String username, InetAddress addr, int port){
        ServerPlayer sp = (ServerPlayer) createEntity(new ServerPlayer(username,2,new Vector2f(0,0)));
        ClientData cd = new ClientData();
        cd.entity=sp;
        cd.address=addr;
        cd.port=port;
        serverPlayers.put(username,cd);
        return sp;
    }
}
