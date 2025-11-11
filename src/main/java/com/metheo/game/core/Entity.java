package com.metheo.game.core;

import java.util.Arrays;
import java.util.Stack;

public class Entity {
    private Game _game;
    private int _id=0;


    public Entity(){
    }

    public void setId(int id){
        if(id==0)return;
        if(_id==0)_id=id;
    }

    public int getId(){
        return _id;
    }

    public void clearId(){
        _id=0;
    }

    public Game getGame(){
        return _game;
    }

    public void setGame(Game game){
        _game=game;
    }
}
