/**
 * Autheur: Theo Bensaci | Date: 18:06 12.11.2025 | Description: Player use by the client in the
 * client
 */
package ch.heig.entity.player;

import ch.heig.core.Entity;
import ch.heig.core.utils.Vector2f;
import ch.heig.entity.SpaceBubble.SpaceBubble;
import ch.heig.network.coreVariant.ClientGame;
import ch.heig.network.networkHandler.INetworkReceiverEntity;
import ch.heig.network.packet.InputPacket;
import ch.heig.network.packet.data.InputData;
import ch.heig.network.packet.data.PacketData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

public class ClientPlayer extends Player implements INetworkReceiverEntity {
  public Vector2f lastDir;
  private final boolean _mainClient;
  public InputData[] inputDataHistroy = new InputData[InputPacket.INPUT_HISTORY_LENGTH];
  private float _clock = 0;
  private int _inputNumber = 0;
  private String _username;
  private int _lastBubbleId = 0;

  public ClientPlayer(int playerColor, Vector2f initPosition, boolean mainClient, String username) {
    super(playerColor, initPosition);
    Arrays.fill(this.inputDataHistroy, new InputData());
    _mainClient = mainClient;
    _username = username;

    outineColor = new Color(0xFFE1D814, true);
  }

  public ClientPlayer(ServerPlayer.Data data) {
    super(1, new Vector2f(data.positionX, data.positionY));
    _mainClient = false;
    _username = data.username;
    outineColor = new Color(0xFF0055);
  }

  @Override
  protected void inputUpdate(float delta) {
    if (!_mainClient) return;
    super.inputUpdate(delta);

    _clock += delta;

    if (lastDir != null && p_targetDir.isEqual(lastDir) && !p_requestDash && !p_requestShoot) {
      return;
    }
    if (lastDir == null) lastDir = new Vector2f(0, 0);

    lastDir.set(p_targetDir);

    ((ClientGame) getGame()).getGameSocket().send(createPacket());
  }

  @Override
  public void draw(Graphics g) {
    if (dead) return;
    ClientGame cp = (ClientGame) getGame();
    float usernameSize = cp.username.length() * 6.5f;
    Vector2f offset = new Vector2f(20 + usernameSize / 2, -5);
    Vector2f usernamePos = getPosition().add(offset);
    g.setColor(outineColor);
    g.fillRect(
        (int) (usernamePos.x - usernameSize / 2),
        (int) (usernamePos.y - 17),
        (int) (usernameSize),
        4);
    ((Graphics2D) g).setStroke(new BasicStroke(3));
    Vector2f lineStartPose =
        new Vector2f(new Vector2f((usernamePos.x - usernameSize / 2), (int) (usernamePos.y - 15)));

    Vector2f diff = getPosition().sub(lineStartPose);

    Vector2f lineEndPos = lineStartPose.copy().add(diff.copy().mult(0.3f));

    g.drawLine(
        (int) (lineStartPose.x),
        (int) (lineStartPose.y),
        (int) (lineEndPos.x),
        (int) (lineEndPos.y));
    g.drawString(_username, (int) (usernamePos.x - usernameSize / 2), (int) (usernamePos.y - 25));
    super.draw(g);
  }

  @Override
  protected void drawNumberOfDash(Graphics g, Vector2f initDashNumberPos) {
    if (_mainClient) {
      super.drawNumberOfDash(g, initDashNumberPos);
    }
  }

  private InputData createInputSnapshot() {
    InputData id = new InputData();
    id.targetDirectionY = Math.round(p_targetDir.y);
    id.targetDirectionX = Math.round(p_targetDir.x);
    id.rotation = p_rotation;
    id.dash = p_requestDash;
    id.delatTimeStart = _clock;
    id.number = _inputNumber;
    id.positionX = p_position.x;
    id.positionY = p_position.y;
    id.shoot = p_requestShoot;
    _inputNumber++;
    _clock = 0;

    // add input data to the history
    addInputDataToHistory(id);

    return id;
  }

  private void addInputDataToHistory(InputData data) {
    InputData buffer, buffer2;
    buffer = inputDataHistroy[0];
    for (int i = 1; i < inputDataHistroy.length; i++) {
      buffer2 = inputDataHistroy[i];
      inputDataHistroy[i] = buffer;
      buffer = buffer2;
    }
    inputDataHistroy[0] = data;
  }

  public InputPacket createPacket() {
    createInputSnapshot();
    InputPacket ip = new InputPacket();
    ip.input = Arrays.copyOf(inputDataHistroy, inputDataHistroy.length);
    ip.username = ((ClientGame) getGame()).username;
    return ip;
  }

  public float getClock() {
    return _clock;
  }

  @Override
  public void applyData(PacketData data) {
    ServerPlayer.Data d = data.safeCast(ServerPlayer.Data.class);
    if (d == null) return;

    p_position.set(d.positionX, d.positionY);
    p_onDash = d.onDash;
    this.p_ammo = d.amo;
    this.p_numberDash = d.numberOfDash;

    if (d.dead != dead) {
      if (d.dead) {
        kill();
      } else {
        dead = false;
      }
    }

    if (!_mainClient) {
      p_rotation = d.rotation;
      playerColor = d.playerColor;
    }

    // bubble effect, i know it's not the good way to do it, but, not time and i'm lazy and i have
    // to many labo to do, so, speed run logic it's for now
    if (d.actualBubbleId != _lastBubbleId) {
      createBubblePartFromId((d.actualBubbleId == 0) ? _lastBubbleId : d.actualBubbleId);
    }
    _lastBubbleId = d.actualBubbleId;
  }

  private void createBubblePartFromId(int id) {
    Entity entity = getGame().getUpdatableEntity(id);
    if (entity == null) return;
    createBubblePart((SpaceBubble) entity);
  }
}
