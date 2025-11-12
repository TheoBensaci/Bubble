package com.metheo.game.core;

import com.metheo.game.core.collision.CollisionBody;
import com.metheo.game.core.collision.CollisionSystem;
import com.metheo.game.core.render.IDrawable;
import com.metheo.game.core.utils.Input;
import com.metheo.game.core.window.Window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Game extends Thread {
    public static boolean DEBUG=false;
    public static final int TARGET_UPDATE_SPEED = 1;           // target update rate in millisecond (honeslty, with a system like this, idk how to make it pretier, sry :[)

    public Window window;
    public Input input;

    // entity gestion
    private final ArrayList<IUpdatable> _updateables=new ArrayList<>();
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

    public Game(boolean createWindow, String title){
        if(createWindow){
            window =new Window(this,title);
        }

        // fill id pull
        for (int i = 5000; i > 0 ; i--) {
            _idPool.push(i);
        }
    }

    public Game(boolean createWindow){
        this(createWindow,"Bubble");
    }


    public void close(){
        _run = false;
        if(window!=null){
            window.dispose();
            window.gameCanvas.close();
        }
    }

    //#region Game state
    public boolean isRunning(){
        return _run;
    }

    /**
     * Discribe if the current game need to comport as a sever or not, check the doc for more information
     * @return if this game is a server or not
     */
    public boolean isServer(){
        return false;
    }

    public float getDeltaTime(){
        return _deltaTime;
    }

    //#endregion


    //#region update

    public void updateEntity(){
        Iterator<IUpdatable> it = _updateables.iterator();
        while (it.hasNext()){
            it.next().update(_deltaTime);
        }
    }

    /**
     * Function call before the start of the update, can be use as a hook
     */
    public void preUpdate(){
        return;
    }

    /**
     * Function call after the end of the update, can be use as a hook
     */
    public void postUpdate(){
        return;
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

                preUpdate();

                long time = System.nanoTime();

                // update entity
                updateEntity();

                // collision
                if(this.isServer())_collisionSystem.doCollisionUpdate();


                // Entity gestion
                updateEntityGestion();

                // delta time calculation
                long i = (1 - (System.nanoTime() - time) / 1000000);
                if (i > 0) {
                    Thread.sleep(i);
                }
                _deltaTime = (float) (float) (System.nanoTime() - time) / 1000000;

                postUpdate();

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    //#endregion



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

    protected void registerEntity(Entity ent){
        // set entity id
        if(ent.getId()==0){
            ent.setId(_idPool.pop());
        }

        if(ent instanceof IUpdatable){
            _updateables.add((IUpdatable)ent);
        }

        if(window !=null) {
            if (ent instanceof IDrawable) {
                window.gameCanvas.registerDrawable((IDrawable) ent);
            }
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.registerBody((CollisionBody)ent);
        }
    }

    protected void unregisterEntity(Entity ent){
        // set entity id
        if(ent.getId()!=0){
            _idPool.push(ent.getId());
        }


        if(ent instanceof IUpdatable){
            _updateables.remove((IUpdatable)ent);
        }

        if(window !=null) {
            if (ent instanceof IDrawable) {
                window.gameCanvas.unregisterDrawable((IDrawable) ent);
            }
        }

        if(ent instanceof CollisionBody){
            _collisionSystem.unregisterBody((CollisionBody)ent);
        }
    }

    public int getNumberOfUpdatable(){
        return _updateables.size();
    }

    @Override
    public void start() {
        _run=true;
        super.start();
    }



    //#endregion


}
