/**
 * Use to manage ressource of the game
 * I want to make ressource load if you need it, for example, if you launche the game UI-less -> no need to load sprite
 * Also i want a system to handle miss loaded ressource, like a missing texture for example
 */
package com.metheo.game.core.ressourceManagement;

import com.metheo.game.core.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RessourceManager {
    public final Map<String,BufferedImage> Textures=new HashMap<>();
    private final BufferedImage _default_texture;

    private static RessourceManager _instance;

    public RessourceManager(){
        _default_texture= new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = _default_texture.createGraphics();
        g.setColor(Color.magenta);
        g.fillRect(0,0,100,100);
        g.setColor(Color.ORANGE);
        g.fillRect(50,50,50,50);
        g.fillRect(0,0,50,50);
        g.dispose();
        /*

        File file = new File(String.valueOf(Objects.requireNonNull(RessourceManager.class.getClassLoader().getResource(path))));

        // load texture
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                //listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }/*
        try{
            _sprite = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("player"+playerNumber+".png")));
        }catch(IOException e){e.printStackTrace();}*/
    }

    public static BufferedImage getTexture(String path){
        RessourceManager r = getRessourceManager();

        if(!Game.isGameOpen() || Game.getGame().Window==null){
            r.Textures.put(path, r._default_texture);
            return r._default_texture;
        }


        return RessourceManager.getAsset(path, r.Textures, data -> {
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


    private static RessourceManager getRessourceManager(){
        if(_instance==null){
            _instance=new RessourceManager();
        }
        return _instance;
    }

}
