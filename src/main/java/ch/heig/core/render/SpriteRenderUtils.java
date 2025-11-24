package ch.heig.core.render;

import ch.heig.core.utils.Vector2f;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class SpriteRenderUtils {

    public static BufferedImage getSpriteFromSpriteSheet(BufferedImage spriteSheet, int x,int y,int w, int h){
        BufferedImage img =new BufferedImage(
                w,
                h,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = img.createGraphics();

        g2.drawImage(spriteSheet,-1*x,-1*y,null);

        g2.dispose();

        return img;
    }

    public static BufferedImage getSpriteFromSpriteSheet(BufferedImage spriteSheet, int w,int h,int index){
        int row =spriteSheet.getWidth()/w;
        int column =spriteSheet.getHeight()/h;
        int x = index%row;
        int y = index/column;
        return getSpriteFromSpriteSheet(spriteSheet,x*w,y*h,w,h);
    }

    public static BufferedImage rotateSprite(BufferedImage sprite, double rad){
        BufferedImage img =new BufferedImage(
                (int)(sprite.getWidth()*1.5f),
                (int)(sprite.getHeight()*1.5f),
                BufferedImage.TYPE_INT_ARGB
        );
        Vector2f recenterOffset=new Vector2f((float) sprite.getWidth() /2, (float) sprite.getHeight() /2);
        Graphics2D g2 = img.createGraphics();
        AffineTransform at = new AffineTransform();
        at.rotate(rad, recenterOffset.x, recenterOffset.y);
        g2.transform(at);
        g2.drawImage(sprite,0,0,null);
        g2.dispose();
        return img;
    }
}
