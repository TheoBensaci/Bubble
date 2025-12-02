package ch.heig.entity.simpleEffect;

import ch.heig.core.render.SpriteRenderUtils;
import ch.heig.core.utils.Vector2f;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PlayerDeathEffect extends SimpleEffect {
  private final Color _color;
  private final double _base_rotation;

  private static final double _MAX_ROTATION = Math.PI / 2;

  public PlayerDeathEffect(Vector2f position, int playerColor) {
    super("textures/bubble_out.png", position, 128, 128, 15, 0.5f, 0);
    Random rand = new Random();
    _color = getColorFromPlayerColor(playerColor);
    _base_rotation = rand.nextDouble(-1 * Math.PI, Math.PI);
  }

  private Color getColorFromPlayerColor(int playerColor) {
    return switch (playerColor) {
      case 0 -> new Color(0xe0dbe4);
      case 1 -> new Color(0xFF5995);
      case 2 -> new Color(0x74faa0);
      case 3 -> new Color(0xffffa3);
      case 4 -> new Color(0xB263FF);
      case 5 -> new Color(0x6edadb);
      case 6 -> new Color(0xFFB700);
      default -> Color.black;
    };
  }

  @Override
  protected BufferedImage getFrame() {
    float t = p_animation.getTime() / p_animation.getMaxTime();
    p_rotation = _base_rotation + _MAX_ROTATION * (1 - Math.pow(1 - t, 5));
    return super.getFrame();
  }

  @Override
  public void draw(Graphics g) {
    ((Graphics2D) g)
        .drawImage(
            getFrame(),
            SpriteRenderUtils.colorToRescaleOp(_color),
            (int) p_position.x - p_frameWidth / 2,
            (int) p_position.y - p_frameHeight / 2);
  }

  @Override
  public int getLayer() {
    return 5;
  }
}
