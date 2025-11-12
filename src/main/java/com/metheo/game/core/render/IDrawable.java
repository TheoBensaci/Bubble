package com.metheo.game.core.render;

import java.awt.*;

public interface IDrawable {
    void draw(Graphics g);

    /**
     * Layer of the entity IDrawable, the higher it is, the latest it will be drawn, usefull to set a "sprite" on top of each other
     * @return layer of entity IDrawable
     */
    int getLayer();
}
