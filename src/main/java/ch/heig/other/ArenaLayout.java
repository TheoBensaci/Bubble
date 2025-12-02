/** Autheur: Theo Bensaci | Date: 18:29 02.12.2025 | Description: Use to define a arena layout */
package ch.heig.other;

import ch.heig.core.utils.Vector2f;

public class ArenaLayout {
  public Vector2f[] spawnPos;
  public BubblePos[] bubbles;

  public static class BubblePos {
    public Vector2f position;
    public float radius;

    public BubblePos(Vector2f position, float radius) {
      this.position = position;
      this.radius = radius;
    }
  }

  public ArenaLayout() {}

  public ArenaLayout setSpawnPos(Vector2f... spawnPos) {
    this.spawnPos = spawnPos;
    return this;
  }

  public ArenaLayout setBubblesPos(BubblePos... bubbles) {
    this.bubbles = bubbles;
    return this;
  }
}
