/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Use to manage ressource of the game
 *                I want to make ressource load if you need it, for example, if you launche the game UI-less -> no need to load sprite
 *                Also i want a system to handle miss loaded ressource, like a missing texture for example
 */


package ch.heig.game.core.ressourceManagement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class RessourceManager {
    public final Map<String,BufferedImage> textures=new HashMap<>();    // list of texture loaded so far
    private final BufferedImage _default_texture;                       // default texture use when missed ressource

    private static RessourceManager _instance;                          // the ressource maanage is singloton

    public RessourceManager(){
        _default_texture= new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = _default_texture.createGraphics();
        g.setColor(Color.magenta);
        g.fillRect(0,0,100,100);
        g.setColor(Color.ORANGE);
        g.fillRect(50,50,50,50);
        g.fillRect(0,0,50,50);
        g.dispose();
    }

    /**
     * Get a texture from the ressource
     * @param path path to the texture (from ressource folder)
     * @return texture founded
     */
    public static BufferedImage getTexture(String path){
        RessourceManager r = getRessourceManager();


        return RessourceManager.getAsset(path, r.textures, data -> {
            if(data==null){
                return r._default_texture;
            }
            BufferedImage result;
            try {
                result = ImageIO.read(data);
            } catch (IOException e) {
                return r._default_texture;
            }
            return result;
        });
    }

    /**
     * generic methods use to find a ressource
     * @param path path to the resource
     * @param map actual map of this resource type
     * @param fnc function use to "decode" the resource if we need to loaded from the files system
     * @return resource finded
     * @param <E> type of the resource
     */
    private static <E> E getAsset(String path, Map<String, E> map, IRessourceLoadFunction<E> fnc){
        E result = map.get(path);
        // if we dont find the texture -> try to find it in ressource
        if(result==null){
            // try to find the texture
            result=fnc.treat(RessourceManager.class.getClassLoader().getResource(path));
            // put the result regardless of the out put, if we didn't find it -> put default value
            map.put(path,result);
        }
        return result;
    }


    /**
     * get the ressource manager
     * @return
     */
    private static RessourceManager getRessourceManager(){
        if(_instance==null){
            _instance=new RessourceManager();
        }
        return _instance;
    }

}
