/** Autheur: Theo Bensaci | Date: 09:21 20.11.2025 | Description: Utils for sprite rendering */
package ch.heig.core.render;

import ch.heig.core.utils.Vector2f;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class SpriteRenderUtils {

  public static BufferedImage getSpriteFromSpriteSheet(
      BufferedImage spriteSheet, int x, int y, int w, int h) {
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2 = img.createGraphics();

    g2.drawImage(spriteSheet, -1 * x, -1 * y, null);

    g2.dispose();

    return img;
  }

  public static BufferedImage getSpriteFromSpriteSheet(
      BufferedImage spriteSheet, int w, int h, int index) {
    int row = spriteSheet.getWidth() / w;
    int column = spriteSheet.getHeight() / h;
    int x = index % row;
    int y = index / row;
    return getSpriteFromSpriteSheet(spriteSheet, x * w, y * h, w, h);
  }

  public static BufferedImage rotateSprite(BufferedImage sprite, double rad) {
    int w = (int) (sprite.getWidth());
    int h = (int) (sprite.getHeight());
    BufferedImage img = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
    Vector2f recenterOffset =
        new Vector2f((float) sprite.getWidth() / 2, (float) sprite.getHeight() / 2);
    Graphics2D g2 = img.createGraphics();
    AffineTransform at = new AffineTransform();
    at.rotate(rad, recenterOffset.x, recenterOffset.y);
    // g2.setColor(Color.gray);
    // g2.fillRect(0,0,sprite.getWidth(),sprite.getHeight());
    g2.transform(at);
    g2.drawImage(sprite, 0, 0, null);
    g2.dispose();
    return img;
  }

  public static RescaleOp colorToRescaleOp(Color col) {
    float[] scales = {
      col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, col.getAlpha() / 255f
    };
    float[] offsets = {0, 0, 0, 0};
    return new RescaleOp(scales, offsets, null);
  }
}
