/**
 * Use to manage ressource of the game
 */
package com.metheo.game.core.ressourceManagement;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RessourceManager {
    public final Map<String,BufferedImage> Textures=new HashMap<>();

    public RessourceManager(){
        /*
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


    private static void iterateOverFolder(){

    }

}
