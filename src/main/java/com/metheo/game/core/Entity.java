package com.metheo.game.core;

public class Entity {
    public Entity(){
        if(Game.Instance==null)return;
        Game.requestEntityCreation(this);
    }

    public void onCreate(){
    }


    public boolean destroy(){
        if(Game.Instance==null)return false;
        Game.requestEntityDestruction(this);
        return true;
    }

    public void onDestroy(){

    }
}
