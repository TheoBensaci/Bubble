/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Class use to manage a instance of a game, it's aim to be the main loop and orginizer of the instance
 */

package ch.heig.game.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import ch.heig.game.core.collision.CollisionBody;
import ch.heig.game.core.collision.CollisionSystem;
import ch.heig.game.core.render.IDrawable;
import ch.heig.game.core.utils.Input;
import ch.heig.game.core.window.Window;

public class Game extends Thread {

    public boolean debug=false;

    public Window window;                                                           // instance of the window use by this instance of game, if there is one
    public Input input;                                                             // input manager of this instance, null if there is no window

    // entity gestion
    private final ArrayList<IUpdatable> _updateables=new ArrayList<>();             // List of entity (can really be any thing other then a Entity as long it implement IUpdatable) to update every tick
    private final ArrayList<Entity> _toBeCreate=new ArrayList<>();                  // list of entity to create
    private final ArrayList<Entity> _toBeDestroy=new ArrayList<>();                 // list of entity to destroy
    private final Semaphore _toBeCreateSemaphore = new Semaphore(1);        // since _toBeCreate can be alter by a other thread, we need to protected
    private final Semaphore _toBeDestroySemaphore = new Semaphore(1);       // since _toBeDestroy can be alter by a other thread, we need to protected


    private final Stack<Integer> _idPool=new Stack<>();                             // id pool use to give a unique id to all entity needing one

    // collision
    private final CollisionSystem _collisionSystem=new CollisionSystem();           // Collision system use to manage collision on this instance

    // thread
    private boolean _run=false;                                                     // if the game is running

    // game state
    private float _deltaTime;                                                       // delta time of the start - end excution of the last game update


    /**
     * Create a game
     * @param createWindow if this instance need a window
     * @param title title of the window if needed one
     */
    public Game(boolean createWindow, String title){
        if(createWindow){
            window =new Window(this,title);
        }

        // fill id pull
        for (int i = 5000; i > 0 ; i--) {
            _idPool.push(i);
        }
    }

    /**
     * Create a game
     * @param createWindow if this instance need a window
     */
    public Game(boolean createWindow){
        this(createWindow,"Bubble");
    }


    /**
     * Close this game
     */
    public void close(){
        _run = false;
        if(window!=null){
            window.dispose();
            window.gameCanvas.close();
        }
    }

    //#region Game state

    /**
     * If this game instance is running
     * @return
     */
    public boolean isRunning(){
        return _run;
    }

    /**
     * Discribe if the current game need to comport as a sever or not, check the doc for more information
     * @return if this game is a server or not
     */
    public boolean isServer(){
        return true;
    }

    /**
     * Get delta time of the last update
     * @return
     */
    public float getDeltaTime(){
        return _deltaTime;
    }

    //#endregion


    //#region update

    /**
     * Update all entity
     */
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

    /**
     * Create a new entity
     * @param e entity to create
     * @return entity to create
     */
    public Entity createEntity(Entity e){
        return createEntity(e,0);
    }


    /**
     * Create a new entity
     * @param e entity to create
     * @param forceId id imposed, if 0 -> generate a new id else set the entity with the force id
     * @return entity to create
     */
    public Entity createEntity(Entity e, int forceId){

        try {
            _toBeCreateSemaphore.acquire();

            // set entity id, we need to do it early to simplfy corrdination
            if(e.getId()==0){
                e.setId((forceId==0)?_idPool.pop():forceId);
            }

            _toBeCreate.add(e);
            _toBeCreateSemaphore.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return e;
    }

    /**
     * Destroy a entity
     * @param e entity to destroy
     */
    public void destroyEntity(Entity e){
        try {
            _toBeDestroySemaphore.acquire();
            _toBeDestroy.add(e);
            _toBeDestroySemaphore.release();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Register a entity to all "service" this entity can be register
     * @param ent entity to register
     */
    protected void registerEntity(Entity ent){

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

    /**
     * Unregister a entity to all "service" this entity can be unregister
     * @param ent entity to register
     */
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

    /**
     * Get the number of entity update every game update
     * @return
     */
    public int getNumberOfUpdatable(){
        return _updateables.size();
    }

    /**
     * Start the game instance
     */
    @Override
    public void start() {
        _run=true;
        super.start();
    }



    //#endregion


}
