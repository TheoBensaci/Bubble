/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Server variant of the game
 */

package ch.heig.network.coreVariant;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import ch.heig.cli.ServerCliUtils;
import ch.heig.core.Entity;
import ch.heig.network.networkHandler.ServerNetworkHandlerSystem;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.ServerPlayer;
import ch.heig.network.ClientData;
import ch.heig.network.packet.PacketType;
import ch.heig.network.packet.SimpleDataPacket;

public class ServerGame extends NetworkGame {

    public final Map<String, ClientData> serverPlayers=new HashMap<>();

    // scoreboard (username -> score)
    private final Map<String, Integer> scoreByPlayer = new LinkedHashMap<>();

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

        // init score
        scoreByPlayer.put(username, 0);

        ServerCliUtils.playerJoinMessage(username,addr,port);

        return sp;
    }

    public void destroyPlayer(String username){
        ClientData cd = serverPlayers.get(username);
        ServerCliUtils.playerExitMessage(username,cd.address,cd.port);
        destroyEntity(cd.entity);
        serverPlayers.remove(username);

        // cleanup score
        scoreByPlayer.remove(username);
    }

    // list all players
    public String[] listPlayers(){
        return serverPlayers.keySet().toArray(new String[0]);
    }

    // send exit to a player and remove it
    public boolean kickPlayer(String username){
        if(!serverPlayers.containsKey(username)) return false;
        ClientData cd = serverPlayers.get(username);
        if(getGameSocket()!=null){
            SimpleDataPacket pkt = new SimpleDataPacket(PacketType.exitServer, null);
            getGameSocket().send(pkt, cd.address, cd.port);
        }
        destroyPlayer(username);
        ServerCliUtils.playerKickedMessage(username);
        return true;
    }

    // send exit to all
    public void broadcastExitToAll(){
        if(getGameSocket()==null) return;
        SimpleDataPacket pkt = new SimpleDataPacket(PacketType.exitServer, null);
        for (Map.Entry<String, ClientData> entry : serverPlayers.entrySet()){
            ClientData cd = entry.getValue();
            getGameSocket().send(pkt, cd.address, cd.port);
        }
    }

    // increment score and log win
    public void addRoundWin(String username){
        if(!scoreByPlayer.containsKey(username)) return;
        int newScore = scoreByPlayer.get(username) + 1;
        scoreByPlayer.put(username, newScore);
        ServerCliUtils.playerWinRoundMessage(username, newScore);
    }

    // format scoreboard for CLI
    public String formatScoreBoard(){
        if(scoreByPlayer.isEmpty()) return "No players / no scores";
        return scoreByPlayer.entrySet().stream()
                .map(e -> e.getKey()+": "+e.getValue())
                .collect(Collectors.joining(", "));
    }
}
