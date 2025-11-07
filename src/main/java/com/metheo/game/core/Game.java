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

    private static Game _instance;

    private final boolean _isServer;

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

    public Game(boolean isServer,boolean createWindow){
        if(createWindow){
            Window=new Window();
        }
        _isServer=isServer;
    }


    /**
     * Get the singoton instance of the game if it existe, else create with the following paramater
     * @param createWindow
     * @return
     */
    public static Game getGame(boolean isServer,boolean createWindow){
        if(_instance!=null)return _instance;
        _instance = new Game(isServer,createWindow);
        _instance.start();
        _instance._run=true;
        return _instance;
    }

    /**
     * Get the singoton instance of the game if it existe, else create it with default parameter
     * @return
     */
    public static Game getGame(){
        if(_instance!=null)return _instance;
        _instance = new Game(true,false);
        _instance.start();
        _instance._run=true;
        return _instance;
    }

    public static boolean isGameOpen(){
        return _instance!=null;
    }

    public void close(){
        _run = false;
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

    public float getDeltaTime(){
        return _deltaTime;
    }


    //#region Entity gestion

    public Entity createEntity(Entity e){
        requestEntityCreation(e);
        e.setGame(this);
        return e;
    }


    public void requestEntityCreation(Entity e) {
        try{
            _semaphoreAdd.acquire();
            _toBeCreate.add(e);
            _semaphoreAdd.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public  void requestEntityDestruction(Entity e) {
        try{
            _semaphoreDestroy.acquire();
            _toBeDestroy.add(e);
            _semaphoreDestroy.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void registerEntity(Entity ent){
        if(ent instanceof IUpdateable){
            _updateables.add((IUpdateable)ent);
        }

        if(Window!=null) {
            if (ent instanceof IDrawable) {
                Window.GameCanvas.registerDrawable((IDrawable) ent);
            }
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.registerBody((CollisionBody)ent);
        }
    }

    private void unregisterEntity(Entity ent){
        if(ent instanceof IUpdateable){
            _updateables.remove((IUpdateable)ent);
        }

        if(Window!=null) {
            if (ent instanceof IDrawable) {
                Window.GameCanvas.unregisterDrawable((IDrawable) ent);
            }
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.unregisterBody((CollisionBody)ent);
        }
    }

    public int getNumberOfUpdateables(){
        return _updateables.size();
    }

    //#endregion

}
