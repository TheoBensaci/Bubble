package com.metheo.game.core.collision;

import java.util.ArrayList;

public class CollisionSystem {

    private final ArrayList<CollisionBody> _bodys=new ArrayList<>();

    public CollisionSystem(){

    }

    public void registerBody(CollisionBody body){
        _bodys.add(body);
    }

    public void unregisterBody(CollisionBody body){
        _bodys.remove(body);
    }

    public void doCollisionUpdate(){
        for (int i = 0; i < _bodys.size(); i++) {
            for (int j = i+1; j < _bodys.size(); j++) {
                _bodys.get(i).collisionStepWith(_bodys.get(j));
            }
        }
    }
}
