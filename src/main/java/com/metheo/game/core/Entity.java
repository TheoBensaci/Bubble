package com.metheo.game.core;

public class Entity {

    private Game _game;

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

    public Game getGame(){
        return _game;
    }

    public void setGame(Game game){
        _game=game;
    }
}
