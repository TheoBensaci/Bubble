/** Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: Server network handler */
package ch.heig.network.networkHandler;

import ch.heig.cli.CliUtils;
import ch.heig.core.Entity;
import ch.heig.network.ClientData;
import ch.heig.network.coreVariant.ServerGame;
import ch.heig.network.packet.*;
import ch.heig.network.packet.data.EntityData;
import ch.heig.network.socket.GameSocket;
import java.util.*;

public class ServerNetworkHandlerSystem extends NetworkHandlerSystem {
  private int _actualGameStateID = 2147483647;

  private static float _TIME_BEFOR_LOGOUT = 500;

  public ServerNetworkHandlerSystem() {
    super();
  }

  @Override
  public void receiveUpdate(GameSocket socket) {
    ServerGame server = (ServerGame) _game;

    Packet[] buffer = new Packet[0];
    try {
      socket.mutex.acquire();
      if (socket.receivedPackets.isEmpty()) {
        socket.mutex.release();
        return;
      }

      buffer = new Packet[socket.receivedPackets.size()];
      socket.receivedPackets.toArray(buffer);
      socket.receivedPackets.clear();
      socket.mutex.release();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    for (Packet p : buffer) {
      switch (p.type) {
        case PacketType.playerInput:
          {
            InputPacket ip = p.safeCast(InputPacket.class);
            if (ip == null) continue;

            if (!server.serverPlayers.containsKey(ip.username)) {
              break;
            }
            ClientData cd = server.serverPlayers.get(ip.username);
            cd.entity.receiveInput(ip.input);
            server.serverPlayers.get(ip.username).lastUpdateClock = 0;
            break;
          }
        case PacketType.exit:
          {
            ExitPacket ep = p.safeCast(ExitPacket.class);
            if (ep == null) continue;
            if (!server.serverPlayers.containsKey(ep.username)) {
              break;
            }
            server.destroyPlayer(ep.username);
            break;
          }
        case PacketType.command:
          {

            // i know normaly this packet is ok, but just for concitency
            CommandPacket cp = p.safeCast(CommandPacket.class);
            if (cp == null) return;

            // we dont need check if the sender as OP pervilege, we all ready did it in the server
            // socket
            execCommand(cp);

            socket.send(cp, cp.inetAddress, cp.port);

            if (cp.commandType == CommandPacket.Command.error) {
              System.out.println(CliUtils.RED_BRIGHT + "[ ERROR ] : " + cp.arg + CliUtils.RESET);
              break;
            } else {
              System.out.println(CliUtils.BLUE_BRIGHT + "[ LOG ] : " + CliUtils.RESET + cp.arg);
            }
            // log the output
            ClientData cd = server.serverPlayers.get(cp.username);
            if (cd != null) cd.logCommandOutput(cp.arg);

            break;
          }
      }
    }
  }

  @Override
  public void senderUpdate(GameSocket socket) {
    if (System.nanoTime() - _lastUpdateSend < _UPDATE_SEND_FREQUENCY) return;
    ServerGame server = (ServerGame) _game;

    // build game state packet for every group
    // will be useful for the lobby system
    Map<Integer, ArrayList<EntityData>> datas = new HashMap();
    for (INetworkSenderEntity e : _sender) {
      int group = ((Entity) e).getGroup();
      ArrayList<EntityData> groupDatas;

      if (!datas.containsKey(group)) {
        groupDatas = new ArrayList<EntityData>();
        datas.put(group, groupDatas);
      } else {
        groupDatas = datas.get(group);
      }

      groupDatas.add(e.getData());
    }

    // send it to all player
    Set<Map.Entry<String, ClientData>> set = server.serverPlayers.entrySet();
    String[] needToBeLogout = new String[set.size()];
    Arrays.fill(needToBeLogout, "");

    Iterator<Map.Entry<String, ClientData>> it = set.iterator();

    for (int i = 0; i < needToBeLogout.length; i++) {
      Map.Entry<String, ClientData> entry = it.next();
      if (entry.getValue().lastUpdateClock < _TIME_BEFOR_LOGOUT) {
        socket.send(
            new GameStatePacket(
                _actualGameStateID,
                datas.get(entry.getValue().entity.getGroup()).toArray(new EntityData[0])),
            entry.getValue().address,
            entry.getValue().port);
        entry.getValue().lastUpdateClock += server.getDeltaTime();
      } else {
        System.out.println(entry.getKey() + " :[");
        needToBeLogout[i] = entry.getKey();
      }
    }

    for (String s : needToBeLogout) {
      if (!s.isEmpty()) {
        server.destroyPlayer(s);
      }
    }

    _lastUpdateSend = System.nanoTime();
    _actualGameStateID++;
  }

  /***
   * Exec a command and send back a log
   * @return result
   */
  private CommandPacket execCommand(CommandPacket commandPacket) {
    ServerGame server = (ServerGame) _game;
    switch (commandPacket.commandType) {
      case CommandPacket.Command.startGame:
        server.startGame();
        commandPacket.arg = "Game start";
        commandPacket.commandType = CommandPacket.Command.log;
        break;
      case CommandPacket.Command.restartGame:
        server.startGame();
        commandPacket.arg = "Game re - started";
        commandPacket.commandType = CommandPacket.Command.log;
        break;

      case CommandPacket.Command.cancelGame:
        server.stopGame();
        commandPacket.arg = "Game stop";
        commandPacket.commandType = CommandPacket.Command.log;
        break;

      case CommandPacket.Command.kickPlayer:
        if (!server.serverPlayers.containsKey(commandPacket.arg)) {
          commandPacket.arg = "player '" + commandPacket.arg + "' is unknown";
          commandPacket.commandType = CommandPacket.Command.error;
          break;
        }

        server.destroyPlayer(commandPacket.arg);
        commandPacket.arg = "Player kick";
        commandPacket.commandType = CommandPacket.Command.log;
        break;

      case CommandPacket.Command.op:
        if (!server.serverPlayers.containsKey(commandPacket.arg)) {
          commandPacket.arg = "player '" + commandPacket.arg + "' is unknown";
          commandPacket.commandType = CommandPacket.Command.error;
          break;
        }

        server.serverPlayers.get(commandPacket.arg).operator = true;
        commandPacket.arg = "player '" + commandPacket.arg + "' is now a operator";
        commandPacket.commandType = CommandPacket.Command.log;
        break;

      case CommandPacket.Command.stopServer:
        commandPacket.arg = "Server stop";
        commandPacket.commandType = CommandPacket.Command.log;
        server.close();
        break;
    }

    return commandPacket;
  }
}
