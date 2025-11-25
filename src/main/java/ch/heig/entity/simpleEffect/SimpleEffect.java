package ch.heig.entity.simpleEffect;

import ch.heig.core.Entity;
import ch.heig.core.IUpdatable;
import ch.heig.core.animation.KeyFrame;
import ch.heig.core.animation.basicAnimation.LerpAnimation;
import ch.heig.core.render.IDrawable;
import ch.heig.core.render.SpriteRenderUtils;
import ch.heig.core.ressourceManagement.RessourceManager;
import ch.heig.core.utils.Vector2f;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleEffect extends Entity implements IDrawable, IUpdatable {

    private final BufferedImage _spriteSheet;
    private final int _frameWidth;
    private final int _frameHeight;
    private final int _nFrame;
    private final Vector2f _position;
    private boolean _hasStart=false;
    private final LerpAnimation _animation;
    private final double _rotation;

    public SimpleEffect(String spriteSheetPath,Vector2f position, int frameWidth,int frameHeight, int nFrame, float duration, double rotation){
        _spriteSheet = RessourceManager.getTexture(spriteSheetPath);
        _frameWidth=frameWidth;
        _frameHeight=frameHeight;
        _nFrame=nFrame;
        _rotation=rotation;
        _position=position.copy();
        _animation=new LerpAnimation(new KeyFrame<>(0f,0f),new KeyFrame<>((float)nFrame,duration));
    }

    @Override
    public void update(float deltaTime) {
        if(!_hasStart) {
            _hasStart=true;
            _animation.start();
        }
        else if(_animation.isStop()){
            getGame().destroyEntity(this);
        }
    }

    @Override
    public void draw(Graphics g) {
        int index = (int)((float)_animation.getValue());
        g.drawImage(
                SpriteRenderUtils.rotateSprite(
                        SpriteRenderUtils.getSpriteFromSpriteSheet(
                                _spriteSheet,
                                _frameWidth,
                                _frameHeight,
                                index
                        ),
                        _rotation
                ), (int)_position.x-_frameWidth/2,(int)_position.y-_frameHeight/2, null);
    }

    @Override
    public int getLayer() {
        return -1;
    }
}
