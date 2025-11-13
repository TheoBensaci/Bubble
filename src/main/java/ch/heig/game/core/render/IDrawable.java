/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Interface use to implement all function needed to render something
 */

package ch.heig.game.core.render;

import java.awt.Graphics;

public interface IDrawable {

    /**
     * Call every frame
     * @param g Graphics context
     */
    void draw(Graphics g);

    /**
     * Layer of the entity IDrawable, the higher it is, the latest it will be drawn, usefull to set a "sprite" on top of each other
     * @return layer of entity IDrawable
     */
    int getLayer();
}
