/**
 * Use to manage ressource of the game
 */
package ch.heig.game.core.ressourceManagement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RessourceManager {
    public final Map<String,BufferedImage> Textures=new HashMap<>();

    public RessourceManager(){

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
