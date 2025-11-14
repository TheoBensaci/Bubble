/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: general entity packet data
 */

package ch.heig.network.packet.data;

import ch.heig.game.core.Entity;

public class EntityData extends PacketData {
    public int id=0;

    public EntityData(Entity ent){
        type=PacketDataType.Entity;
        id=ent.getId();
    }
}
