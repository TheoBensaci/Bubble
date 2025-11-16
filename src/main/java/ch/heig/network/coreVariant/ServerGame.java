/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Server variant of the game
 */

package ch.heig.network.coreVariant;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import ch.heig.cli.ServerCliUtils;
import ch.heig.core.Entity;
import ch.heig.network.networkHandler.ServerNetworkHandlerSystem;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.ServerPlayer;
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
        cd.lastUpdateClock=0;
        serverPlayers.put(username,cd);

        ServerCliUtils.playerJoinMessage(username,addr,port);

        return sp;
    }

    public void destroyPlayer(String username){
        ClientData cd = serverPlayers.get(username);
        ServerCliUtils.playerExitMessage(username,cd.address,cd.port);
        destroyEntity(cd.entity);
        serverPlayers.remove(username);
    }
}
