/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Entity of the game, use to implement id, tag, group. Check the doc for more information.
 */

package ch.heig.core;

public class Entity {
    private Game _game;
    private int _id=0;
    private int _group=0;
    public Tag tag=Tag.none;
    private boolean _toBeDestroy=false;


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

    /**
     * Get the entity group of this entity
     * @return
     */
    public int getGroup(){
        return _group;
    }

    public void setGroup(int group){
        _group=group;
    }

    public void markAsDestroy(){
        _toBeDestroy=true;
    }

    public boolean isDestroy(){
        return _toBeDestroy;
    }
}
