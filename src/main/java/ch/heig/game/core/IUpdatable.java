/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Interface use to implement all a class need to be update every game tick
 */

package ch.heig.game.core;

public interface IUpdatable {

    /**
     * Call every game update
     * @param deltaTime delta time of the last game update
     */
    void update(float deltaTime);
}
