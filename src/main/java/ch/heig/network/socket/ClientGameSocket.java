/**
 * Autheur: Theo Bensaci | Date: 07:55 26.11.2025 | Description: Class use to add behavior to the
 * game socket specific for the client, for example, login
 */
package ch.heig.network.socket;

import ch.heig.network.coreVariant.ClientGame;
import ch.heig.network.packet.CommandPacket;
import ch.heig.network.packet.ExitPacket;
import ch.heig.network.packet.LoginPacket;
import ch.heig.network.packet.Packet;
import ch.heig.network.packet.PacketType;
import ch.heig.network.packet.PingPacket;

public class ClientGameSocket extends GameSocket {
  private ClientGame _game;

  // Ping gestion
  private boolean _pingRunning = false;
  private long _lastPingSend = 0;

  // command send by this client wait to be resolve
  private static final long _RESEND_TIME = 100;
  private CommandPacket _actualCommand;
  private int _commandId = 0;
  private long _lastCommandTime = 0;
  private boolean _lastCommandStatue = false; // false = waiting, true = answered

  // Login gestion
  private LoginPacket _actualLoginPacket;
  private boolean _onLoginWait = false;
  private long _lastLoginTime = 0;

  public ClientGameSocket(String hostName, int targetDefaultPort, int listenDefaultPort) {
    super(hostName, targetDefaultPort, listenDefaultPort);
  }

  public void setGame(ClientGame game) {
    _game = game;
  }

  @Override
  public void run() {
    // TODO : ADD LOGIN BEHAVIORE

    super.run();
  }

  @Override
  protected boolean packetPreProcess(Packet packet) {
    switch (packet.type) {
      case PacketType.command:
        CommandPacket cp = packet.safeCast(CommandPacket.class);
        if (cp == null) return false;
        if (cp.index != _commandId - 1) {
          return false;
        }
        _actualCommand = cp;
        _lastCommandStatue = true;

        return false;

      case PacketType.login:
        LoginPacket lp = packet.safeCast(LoginPacket.class);
        if (lp == null) return false;
        if (_onLoginWait) {
          _onLoginWait = false;
          _actualLoginPacket = lp;
        }
        return false;

      case PacketType.ping:
        if (_pingRunning) {
          _pingRunning = false;
        }
        return false;
      case PacketType.exit:
        _game.close();
        return false;

      default:
        return super.packetPreProcess(packet);
    }
  }

  public void sendCommand(CommandPacket.Command commandType, String arg) {
    _actualCommand = new CommandPacket(_game.username, commandType, arg, _commandId);
    _lastCommandTime = System.currentTimeMillis();
    _lastCommandStatue = false;
    _commandId++;
    send(_actualCommand);
  }

  public boolean getLastCommandStatue() {
    return _lastCommandStatue;
  }

  public void commandRoutine() {
    if (_lastCommandStatue) return;
    if (System.currentTimeMillis() - _lastCommandTime > _RESEND_TIME) {
      _lastCommandTime = System.currentTimeMillis();
      send(_actualCommand);
    }
  }

  public CommandPacket getActualCommand() {
    return _actualCommand;
  }

  public void startPing() {
    sendPing();
    _pingRunning = true;
  }

  public void stopPing() {
    _pingRunning = false;
  }

  public void sendPing() {
    if (!_pingRunning) return;
    _lastPingSend = System.currentTimeMillis();
    send(new PingPacket());
  }

  public boolean pingStatue() {
    return _pingRunning;
  }

  public void pingRoutine() {
    if (System.currentTimeMillis() - _lastPingSend > _RESEND_TIME) {
      sendPing();
    }
  }

  public void login(String username, int playerColor) {
    if (_onLoginWait) return;
    _actualLoginPacket = new LoginPacket(username, playerColor);
    _onLoginWait = true;
    sendLoginPacket();
  }

  private void sendLoginPacket() {
    if (!_onLoginWait) return;
    send(_actualLoginPacket);
    _lastLoginTime = System.currentTimeMillis();
  }

  public boolean onLoginWait() {
    return _onLoginWait;
  }

  public void loginRoutine() {
    if (!_onLoginWait) return;
    if (System.currentTimeMillis() - _lastLoginTime > _RESEND_TIME) {
      sendLoginPacket();
    }
  }

  public LoginPacket getActualLoginPacket() {
    return _actualLoginPacket;
  }

  @Override
  public void close() {
    send(new ExitPacket(_game.username));
    super.close();
  }
}
