/**
 *   Autheur: Theo Bensaci
 *   Date: 15:52 13.11.2025
 *   Description: Data use to manage client on server side
 */

package ch.heig.network;

import java.net.InetAddress;

import ch.heig.entity.player.ServerPlayer;

public class ClientData {
    public ServerPlayer entity;
    public InetAddress address;
    public int port;
    public float lastUpdateClock=0;
}
