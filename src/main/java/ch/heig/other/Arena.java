/**
 * Autheur: Theo Bensaci | Date: 20:53 16.11.2025 | Description: Class use to manage the Arena or
 * the limit of the playable area
 */
package ch.heig.other;

import ch.heig.core.render.GameRender;
import ch.heig.core.utils.Vector2f;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Arena {
  private static final Color _OUTLINE_COLOR = new Color(0x505081);
  private static final Color _BACKRGOUND_COLOR = new Color(0x0C0C17);

  public static boolean active = true;
  public static float radiuse = 200f;
  public static Vector2f position = new Vector2f(0, 0);

  public static void drawBackground(Graphics g) {
    g.setColor(_BACKRGOUND_COLOR);
    ((Graphics2D) g).setStroke(new BasicStroke(3));
    g.fillOval(
        (int) (position.x - radiuse),
        (int) (position.y - radiuse),
        (int) radiuse * 2,
        (int) radiuse * 2);
  }

  public static void drawOutline(Graphics g) {
    g.setColor(GameRender.BACKGROUND_COLOR);
    int n = 20;
    ((Graphics2D) g).setStroke(new BasicStroke(n * 2));
    g.drawOval(
        (int) (position.x - radiuse - n),
        (int) (position.y - radiuse - n),
        (int) radiuse * 2 + n * 2,
        (int) radiuse * 2 + n * 2);

    g.setColor(_OUTLINE_COLOR);
    ((Graphics2D) g).setStroke(new BasicStroke(3));
    g.drawOval(
        (int) (position.x - radiuse),
        (int) (position.y - radiuse),
        (int) radiuse * 2,
        (int) radiuse * 2);
  }

  public static Vector2f getPosition() {
    return position.copy();
  }
}
