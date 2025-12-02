/**
 * Autheur: Theo Bensaci | Date: 10:19 24.11.2025 | Description: Entity use to display simple effect
 * like explosion or "particle" When the animation is over, this entity destroy If the game as no
 * render -> instant destroy on creation
 */
package ch.heig.entity.simpleEffect;

import ch.heig.core.Entity;
import ch.heig.core.IUpdatable;
import ch.heig.core.animation.KeyFrame;
import ch.heig.core.animation.basicAnimation.LerpAnimation;
import ch.heig.core.render.IDrawable;
import ch.heig.core.render.SpriteRenderUtils;
import ch.heig.core.ressourceManagement.RessourceManager;
import ch.heig.core.utils.Vector2f;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SimpleEffect extends Entity implements IDrawable, IUpdatable {

  protected final BufferedImage p_spriteSheet;
  protected final int p_frameWidth;
  protected final int p_frameHeight;
  protected final int p_nFrame;
  protected final Vector2f p_position;
  private boolean _hasStart = false;
  protected final LerpAnimation p_animation;
  protected double p_rotation;

  public SimpleEffect(
      String spriteSheetPath,
      Vector2f position,
      int frameWidth,
      int frameHeight,
      int nFrame,
      float duration,
      double rotation) {
    p_spriteSheet = RessourceManager.getTexture(spriteSheetPath);
    p_frameWidth = frameWidth;
    p_frameHeight = frameHeight;
    p_nFrame = nFrame;
    p_rotation = rotation;
    p_position = position.copy();
    p_animation =
        new LerpAnimation(new KeyFrame<>(0f, 0f), new KeyFrame<>((float) nFrame, duration));
  }

  @Override
  public void update(float deltaTime) {
    if (!_hasStart) {
      _hasStart = true;
      p_animation.start();
    } else if (p_animation.isStop()) {
      getGame().destroyEntity(this);
    }
  }

  @Override
  public void draw(Graphics g) {
    g.drawImage(
        getFrame(),
        (int) p_position.x - p_frameWidth / 2,
        (int) p_position.y - p_frameHeight / 2,
        null);
  }

  protected BufferedImage getFrame() {
    int index = (int) ((float) p_animation.getValue());
    return SpriteRenderUtils.rotateSprite(
        SpriteRenderUtils.getSpriteFromSpriteSheet(
            p_spriteSheet, p_frameWidth, p_frameHeight, index),
        p_rotation);
  }

  @Override
  public int getLayer() {
    return -1;
  }
}
