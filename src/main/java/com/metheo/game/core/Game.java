package com.metheo.game.core;

import com.metheo.game.core.collision.CollisionBody;
import com.metheo.game.core.collision.CollisionSystem;
import com.metheo.game.core.render.IDrawable;
import com.metheo.game.core.window.Window;
import com.metheo.network.GameSocket;
import com.metheo.network.INetworkSenderEntity;
import com.metheo.network.NetworkHandlerSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Game extends Thread {
    public static boolean DEBUG=false;
    public static final int TARGET_UPDATE_SPEED = 1;           // target update rate in millisecond (honeslty, with a system like this, idk how to make it pretier, sry :[)

    private static Game _instance;

    private final boolean _isServer;

    public Window window;

    // entity gestion
    private final ArrayList<IUpdateable> _updateables=new ArrayList<>();
    private final ArrayList<Entity> _toBeCreate=new ArrayList<>();
    private final ArrayList<Entity> _toBeDestroy=new ArrayList<>();
    private final Semaphore _toBeCreateSemaphore = new Semaphore(1);
    private final Semaphore _toBeDestroySemaphore = new Semaphore(1);


    private final Stack<Integer> _idPool=new Stack<>();

    // collision
    private final CollisionSystem _collisionSystem=new CollisionSystem();

    // thread
    private boolean _run=false;

    // game state
    private float _deltaTime;

    // networking
    private NetworkHandlerSystem _networkHandler;
    private GameSocket _gameSocket;


    public Game(boolean isServer,boolean createWindow){
        if(createWindow){
            window =new Window();
        }
        _isServer=isServer;

        // fill id pull
        for (int i = 5000; i > 0 ; i--) {
            _idPool.push(i);
        }

        _networkHandler=new NetworkHandlerSystem(this);
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
        if(_gameSocket!=null){
            _gameSocket.close();
        }
        if(window!=null){
            window.GameCanvas.close();
        }
    }


    //#region update

    public void updateEntity(){
        Iterator<IUpdateable> it = _updateables.iterator();
        while (it.hasNext()){
            it.next().update(_deltaTime);
        }
    }


    /**
     * Manage entity creation and destruction
     */
    public void updateEntityGestion(){
        try {
            _toBeCreateSemaphore.acquire();
            for(Entity e : _toBeCreate){
                e.setGame(this);
                registerEntity(e);
            }
            _toBeCreate.clear();
            _toBeCreateSemaphore.release();


            _toBeDestroySemaphore.acquire();
            for(Entity e : _toBeDestroy){
                unregisterEntity(e);
            }
            _toBeDestroy.clear();
            _toBeDestroySemaphore.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {

        try {
            while (_run) {

                // feetch socket data
                if(_gameSocket!=null){
                    _networkHandler.receveUpdate(_gameSocket);
                }

                long time = System.nanoTime();

                // update entity
                updateEntity();

                // collision
                _collisionSystem.doCollisionUpdate();


                // Entity gestion
                updateEntityGestion();

                // delta time calculation
                long i = (1 - (System.nanoTime() - time) / 1000000);
                if (i > 0) {
                    Thread.sleep(i);
                }
                _deltaTime = (float) (float) (System.nanoTime() - time) / 1000000;

                // send update data
                if(_gameSocket!=null){
                    _networkHandler.senderUpdate(_gameSocket);
                }

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    //#endregion

    public float getDeltaTime(){
        return _deltaTime;
    }


    //#region Entity gestion

    public Entity createEntity(Entity e){
        try {
            _toBeCreateSemaphore.acquire();
            _toBeCreate.add(e);
            _toBeCreateSemaphore.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }

    public void destroyEntity(Entity e){
        try {
            _toBeDestroySemaphore.acquire();
            _toBeDestroy.add(e);
            _toBeDestroySemaphore.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void forceCreate(Entity entity){
        registerEntity(entity);
    }



    private void registerEntity(Entity ent){
        // set entity id
        if(ent.getId()==0){
            ent.setId(_idPool.pop());
        }

        if(ent instanceof IUpdateable){
            _updateables.add((IUpdateable)ent);
        }

        _networkHandler.registerNetworkEntity(ent);

        if(window !=null) {
            if (ent instanceof IDrawable) {
                window.GameCanvas.registerDrawable((IDrawable) ent);
            }
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.registerBody((CollisionBody)ent);
        }
    }

    private void unregisterEntity(Entity ent){
        // set entity id
        if(ent.getId()!=0){
            _idPool.push(ent.getId());
        }


        if(ent instanceof IUpdateable){
            _updateables.remove((IUpdateable)ent);
        }

        _networkHandler.unregisterNetworkEntity(ent);


        if(window !=null) {
            if (ent instanceof IDrawable) {
                window.GameCanvas.unregisterDrawable((IDrawable) ent);
            }
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.unregisterBody((CollisionBody)ent);
        }
    }

    public int getNumberOfUpdateables(){
        return _updateables.size();
    }

    public boolean isRunning(){
        return _run;
    }

    public boolean isServer(){
        return _isServer;
    }

    //#endregion


    //#region network

    public void setGameSocket(GameSocket gameSocket){
        _gameSocket=gameSocket;
        _gameSocket.start();
    }

    public GameSocket getGameSocket(){
        return _gameSocket;
    }


    //#endregion

}
