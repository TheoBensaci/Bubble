package ch.heig.game.core.render;

import java.awt.*;

public interface IDrawable {
    void draw(Graphics g);

    int getLayer();
}
