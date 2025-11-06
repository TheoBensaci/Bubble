package com.metheo.game.core;

import com.metheo.game.core.collision.CollisionBody;
import com.metheo.game.core.collision.CollisionSystem;
import com.metheo.game.core.render.GameRender;
import com.metheo.game.core.render.IDrawable;
import com.metheo.game.core.ressourceManagement.RessourceManager;
import com.metheo.game.core.window.Window;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Game extends Thread {
    public static boolean DEBUG=false;
    public static final int TARGET_UPDATE_SPEED = 1;           // target update rate in millisecond (honeslty, with a system like this, idk how to make it pretier, sry :[)

    public static Game Instance;

    public Window Window;

    // entity gestion
    public final ArrayList<IUpdateable> _updateables=new ArrayList<>();
    public final ArrayList<Entity> _toBeCreate=new ArrayList<>();
    public final ArrayList<Entity> _toBeDestroy=new ArrayList<>();

    // collision
    public final CollisionSystem _collisionSystem=new CollisionSystem();

    // thread
    private final Semaphore _semaphoreAdd=new Semaphore(1);
    private final Semaphore _semaphoreDestroy=new Semaphore(1);
    private boolean _run=false;

    // game state
    private float _deltaTime;

    private Game(boolean createWindow){
        if(createWindow){
            Window=new Window();
        }
    }


    public static Game creatGame(boolean createWindow){
        if(Instance!=null)return Instance;
        Instance = new Game(createWindow);
        Instance.start();
        Instance._run=true;
        return Instance;
    }

    public static void close(){
        if(Game.Instance==null)return;
        Game.Instance._run=false;
    }

    @Override
    public void run() {
        try {
            while (_run) {
                long time = System.nanoTime();

                for (IUpdateable updateable : _updateables) {
                    updateable.update(_deltaTime);
                }

                // collision
                _collisionSystem.doCollisionUpdate();


                // creation and destruction of entity
                if(!_toBeCreate.isEmpty()){
                    _semaphoreAdd.acquire();
                    for (Entity e : _toBeCreate){
                        e.onCreate();
                        registerEntity(e);
                    }
                    _toBeCreate.clear();
                    _semaphoreAdd.release();
                }


                if(!_toBeDestroy.isEmpty()){
                    _semaphoreDestroy.acquire();
                    for (Entity e : _toBeDestroy){
                        e.onDestroy();
                        unregisterEntity(e);
                    }
                    _toBeDestroy.clear();
                    _semaphoreDestroy.release();
                }


                long i = (1 - (System.nanoTime() - time) / 1000000);

                if (i > 0) {
                    Thread.sleep(i);
                }
                _deltaTime = (float) (float) (System.nanoTime() - time) / 1000000;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static float getDeltaTime(){
        if(Instance==null)return 0;
        return Instance._deltaTime;
    }


    //#region Entity gestion


    public static void requestEntityCreation(Entity e) {
        if(Instance==null)return;
        try{
            Instance._semaphoreAdd.acquire();
            Instance._toBeCreate.add(e);
            Instance._semaphoreAdd.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void requestEntityDestruction(Entity e) {
        if(Instance==null)return;
        try{
            Instance._semaphoreDestroy.acquire();
            Instance._toBeDestroy.add(e);
            Instance._semaphoreDestroy.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void registerEntity(Entity ent){
        if(ent instanceof IUpdateable){
            _updateables.add((IUpdateable)ent);
        }

        if(ent instanceof IDrawable){
            GameRender.registerDrawable((IDrawable)ent);
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.registerBody((CollisionBody)ent);
        }
    }

    private void unregisterEntity(Entity ent){
        if(ent instanceof IUpdateable){
            _updateables.remove((IUpdateable)ent);
        }

        if(ent instanceof IDrawable){
            GameRender.unregisterDrawable((IDrawable)ent);
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.unregisterBody((CollisionBody)ent);
        }
    }

    public static int getNumberOfUpdateables(){
        if(Instance==null)return 0;
        return Instance._updateables.size();
    }

    //#endregion

}
