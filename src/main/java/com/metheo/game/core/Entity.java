package com.metheo.game.core;

public class Entity {
    public Entity(){
    }

    public void onCreate(){
    }


    public boolean destroy(Game game){
        game.requestEntityDestruction(this);
        return true;
    }

    public void onDestroy(){

    }
}
