/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Server variant of the game
 */

package ch.heig.network.coreVariant;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.heig.cli.ServerCliUtils;
import ch.heig.core.Entity;
import ch.heig.network.networkHandler.ServerNetworkHandlerSystem;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.ServerPlayer;
import ch.heig.network.ClientData;
import ch.heig.network.packet.LoginPacket;
import ch.heig.network.socket.GameSocket;
import ch.heig.network.socket.ServerGameSocket;

public class ServerGame extends NetworkGame {

    public static final int PLAYER_LIMIT = 10;
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

    public Entity createNewPlayer(String username, int playerColor, InetAddress addr, int port){
        ServerPlayer sp = (ServerPlayer) createEntity(new ServerPlayer(username,playerColor,new Vector2f(0,0)));
        ClientData cd = new ClientData();
        cd.entity=sp;
        cd.address=addr;
        cd.port=port;
        cd.lastUpdateClock=0;
        cd.operator=serverPlayers.isEmpty();
        serverPlayers.put(username,cd);

        ServerCliUtils.playerJoinMessage(username,addr,port);

        return sp;
    }

    public void destroyPlayer(String username){
        ClientData cd = serverPlayers.get(username);
        ServerCliUtils.playerExitMessage(username,cd.address,cd.port);
        destroyEntity(cd.entity);
        serverPlayers.remove(username);

        // Check if there is still on with op privilege

        if(serverPlayers.isEmpty())return;

        Set<Map.Entry<String,ClientData>> set = serverPlayers.entrySet();

        for (Map.Entry<String,ClientData> d : set){
            if(d.getValue().operator){
                return;
            }
        }
        set.toArray(new ClientData[0])[0].operator=true;
    }

    public boolean isLoginPacketError(LoginPacket loginPacket){
        if(!serverPlayers.containsKey(loginPacket.username))return false;

        ClientData cd = serverPlayers.get(loginPacket.username);
        if(!cd.checkIfPacketProvnence(loginPacket))return true;

        return false;
    }

    @Override
    public void setGameSocket(GameSocket gameSocket) {
        if(gameSocket instanceof ServerGameSocket serverGameSocket){
            serverGameSocket.setGame(this);
        }
        super.setGameSocket(gameSocket);
    }
}
