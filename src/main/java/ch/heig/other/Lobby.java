/** Autheur: Theo Bensaci | Date: 18:24 02.12.2025 | Description: Use to manage a lobby */
package ch.heig.other;

import ch.heig.core.Game;
import ch.heig.core.render.GameRender;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.SpaceBubble.SpaceBubble;
import ch.heig.network.ClientData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Lobby {

  // DEFINE LOBBY BUBBLE
  private static final ArenaLayout[] _LAYOUTS = {
    new ArenaLayout()
        .setBubblesPos(
            new ArenaLayout.BubblePos(
                new Vector2f(GameRender.WIDTH / 2, GameRender.HEIGHT / 2), 100),
            new ArenaLayout.BubblePos(
                new Vector2f(GameRender.WIDTH / 2 + 200, GameRender.HEIGHT / 2), 50),
            new ArenaLayout.BubblePos(
                new Vector2f(GameRender.WIDTH / 2 - 200, GameRender.HEIGHT / 2), 50),
            new ArenaLayout.BubblePos(
                new Vector2f(GameRender.WIDTH / 2, GameRender.HEIGHT / 2 + 200), 50),
            new ArenaLayout.BubblePos(
                new Vector2f(GameRender.WIDTH / 2, GameRender.HEIGHT / 2 - 200), 50))
        .setSpawnPos(
            new Vector2f(GameRender.WIDTH / 2 + 200, GameRender.HEIGHT / 2),
            new Vector2f(GameRender.WIDTH / 2 - 200, GameRender.HEIGHT / 2),
            new Vector2f(GameRender.WIDTH / 2, GameRender.HEIGHT / 2 + 200),
            new Vector2f(GameRender.WIDTH / 2, GameRender.HEIGHT / 2 - 200))
  };

  private boolean _setup = false;
  private final ArrayList<SpaceBubble> _spaceBubbles = new ArrayList<>();
  private final int _groupLinked;

  private Vector2f[] _spawnPos;

  private ClientData[] _players = new ClientData[2];
  private Stack<ClientData> _queue = new Stack<>();
  private final Game _game;
  private boolean _isMatchInGoing = false;

  public Lobby(Game game, int groupLinked) {
    _groupLinked = groupLinked;
    _game = game;
  }

  public void setUpArena() {
    Random rand = new Random();
    setUpArena(_LAYOUTS[rand.nextInt(_LAYOUTS.length)]);
  }

  public void setUpArena(ArenaLayout layout) {
    if (_setup) {
      for (SpaceBubble bubble : _spaceBubbles) {
        _game.destroyEntity(bubble);
      }
      _spaceBubbles.clear();
    }
    // chose layout
    _spawnPos = layout.spawnPos;

    for (ArenaLayout.BubblePos bubblePos : layout.bubbles) {
      _spaceBubbles.add(
          (SpaceBubble)
              _game.createEntity(
                  new SpaceBubble(bubblePos.position, bubblePos.radius), _groupLinked));
    }

    _setup = true;
  }

  public void startFight(boolean forceRandomPos) {
    if (_players[0] == null || _players[1] == null) return;
    for (SpaceBubble bubble : _spaceBubbles) {
      bubble.fullHeal();
    }

    for (ClientData cd : _queue) {
      cd.entity.dead = true;
    }

    ClientData cd =
        (!_players[0].entity.dead) ? _players[0] : (!_players[1].entity.dead) ? _players[1] : null;
    Vector2f pos;
    if (cd == null || forceRandomPos) {
      cd = _players[0];
      pos = getRandomSpawnPoint();
    } else {
      pos = cd.entity.getPosition();
    }

    cd.entity.setPosition(pos);
    cd.entity.revive();
    cd = getOtherPlayer(cd);
    pos = getFarestSpawnPoint(pos);

    cd.entity.setPosition(pos);
    cd.entity.revive();
    _isMatchInGoing = true;
  }

  public Vector2f getFarestSpawnPoint(Vector2f pos) {
    float maxDistance = 0f;
    int index = 0;
    for (int i = 0; i < _spawnPos.length; i++) {
      float distance = pos.copy().sub(_spawnPos[i]).powMagn();
      if (distance > maxDistance) {
        maxDistance = distance;
        index = i;
      }
    }
    return _spawnPos[index];
  }

  public Vector2f getRandomSpawnPoint() {
    Random rand = new Random();
    return _spawnPos[rand.nextInt(_spawnPos.length)];
  }

  public boolean isMatchInGoing() {
    return _isMatchInGoing;
  }

  public boolean canStart() {
    return (!_isMatchInGoing && _players[0] != null && _players[1] != null);
  }

  public ClientData getWinner() {
    if (_players[0] == null || _players[1] == null) return null;

    if (_players[0].entity.dead) {
      return _players[1];
    }

    if (_players[1].entity.dead) {
      return _players[0];
    }

    return null;
  }

  /**
   * Get the other player, for example, if you set cd = player[0] then return player[1]
   *
   * @param cd player to not return
   * @return the other player
   */
  public ClientData getOtherPlayer(ClientData cd) {
    if (_players[0].equals(cd)) {
      return _players[1];
    }
    if (_players[1].equals(cd)) {
      return _players[0];
    }
    return null;
  }

  public void endMatch() {
    _isMatchInGoing = false;
  }

  public void moveIntoQueue(ClientData cd) {
    if (_players[0].equals(cd)) {
      _queue.push(cd);
      _players[0] = _queue.pop();
      return;
    }
    if (_players[1].equals(cd)) {
      _queue.push(cd);
      _players[1] = _queue.pop();
    }
  }

  public void putClient(ClientData cd) {
    for (int i = 0; i < _players.length; i++) {
      if (_players[i] == null) {
        _players[i] = cd;
        _game.changeEntityGroup(cd.entity, _groupLinked);
        return;
      }
    }

    _queue.push(cd);

    _game.changeEntityGroup(cd.entity, _groupLinked);
  }

  /**
   * Pop a client from this lobby
   *
   * @param cd
   * @return if the player was in this lobby
   */
  public void popClient(ClientData cd) {
    for (int i = 0; i < _players.length; i++) {
      if (_players[i].equals(cd)) {
        _players[i] = null;
        return;
      }
    }

    _queue.remove(cd);
  }

  public List<ClientData> getPlayers() {
    ArrayList<ClientData> returnValue = new ArrayList<>();
    returnValue.add(_players[0]);
    returnValue.add(_players[1]);
    returnValue.addAll(_queue);
    return returnValue;
  }
}
