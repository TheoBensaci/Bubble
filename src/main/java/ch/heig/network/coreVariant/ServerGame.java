/** Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: Server variant of the game */
package ch.heig.network.coreVariant;

import ch.heig.cli.ServerCliUtils;
import ch.heig.core.Entity;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.player.ServerPlayer;
import ch.heig.network.ClientData;
import ch.heig.network.networkHandler.ServerNetworkHandlerSystem;
import ch.heig.network.packet.LoginPacket;
import ch.heig.network.socket.GameSocket;
import ch.heig.network.socket.ServerGameSocket;
import ch.heig.other.Lobby;
import java.net.InetAddress;
import java.util.*;

public class ServerGame extends NetworkGame {

  public static final int PLAYER_LIMIT = 10;
  public final Map<String, ClientData> serverPlayers = new HashMap<>();
  public final Lobby _lobby;
  private long _lastWinTime = 0;
  private boolean _gameStart = false;

  public ServerGame(boolean createWindow, String title) {
    super(createWindow, title, new ServerNetworkHandlerSystem());
    _lobby = new Lobby(this, 0);
    _lobby.setUpArena();
  }

  public ServerGame(boolean createWindow) {
    this(createWindow, "Bubble - Server");
  }

  @Override
  public boolean isServer() {
    return true;
  }

  public Entity createNewPlayer(String username, int playerColor, InetAddress addr, int port) {
    ServerPlayer sp =
        (ServerPlayer) createEntity(new ServerPlayer(username, playerColor, new Vector2f(0, 0)));
    ClientData cd = new ClientData();
    cd.entity = sp;
    cd.address = addr;
    cd.port = port;
    cd.lastUpdateClock = 0;
    cd.operator = serverPlayers.isEmpty();
    serverPlayers.put(username, cd);

    ServerCliUtils.playerJoinMessage(username, addr, port);

    _lobby.putClient(cd);

    return sp;
  }

  public void destroyPlayer(String username) {
    ClientData cd = serverPlayers.get(username);
    ServerCliUtils.playerExitMessage(username, cd.address, cd.port);
    destroyEntity(cd.entity);
    serverPlayers.remove(username);

    // Check if there is still on with op privilege

    if (serverPlayers.isEmpty()) return;

    Set<Map.Entry<String, ClientData>> set = serverPlayers.entrySet();

    for (Map.Entry<String, ClientData> d : set) {
      if (d.getValue().operator) {
        return;
      }
    }
    for (Map.Entry<String, ClientData> d : set) {
      if (!d.getValue().operator) {
        d.getValue().operator = true;
        return;
      }
    }
  }

  public boolean isLoginPacketError(LoginPacket loginPacket) {
    if (!serverPlayers.containsKey(loginPacket.username)) return false;

    ClientData cd = serverPlayers.get(loginPacket.username);
    if (!cd.checkIfPacketProvnence(loginPacket)) return true;

    return false;
  }

  @Override
  public void setGameSocket(GameSocket gameSocket) {
    if (gameSocket instanceof ServerGameSocket serverGameSocket) {
      serverGameSocket.setGame(this);
    }
    super.setGameSocket(gameSocket);
  }

  @Override
  public void preUpdate() {
    super.preUpdate();

    // lobby gestion
    if (!_gameStart) {
      Set<Map.Entry<String, ClientData>> set = serverPlayers.entrySet();
      for (Map.Entry<String, ClientData> d : set) {
        if (d.getValue().entity.dead) {
          d.getValue().entity.revive();
        }
      }
      return;
    }
    if (_lobby.isMatchInGoing()) {
      ClientData winner = _lobby.getWinner();
      if (winner != null) {
        _lobby.endMatch();
        winner.score++;
        winner.entity.revive();
        _lobby.moveIntoQueue(_lobby.getOtherPlayer(winner));
        _lastWinTime = System.currentTimeMillis();
      }
    } else if (_lobby.canStart() && System.currentTimeMillis() - _lastWinTime > 1000) {
      _lobby.startFight(false);
    }
  }

  public void startGame() {
    _gameStart = true;
    _lobby.setUpArena();
    Set<Map.Entry<String, ClientData>> set = serverPlayers.entrySet();
    for (Map.Entry<String, ClientData> d : set) {
      d.getValue().score = 0;
    }
    _lobby.startFight(true);
  }

  public void stopGame() {
    _gameStart = false;
  }
}
