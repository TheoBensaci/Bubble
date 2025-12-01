/**
 *   Autheur: Theo Bensaci
 *   Date: 15:52 13.11.2025
 *   Description: Data use to manage client on server side
 */

package ch.heig.network;

import java.net.InetAddress;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.heig.entity.player.ServerPlayer;
import ch.heig.network.packet.CommandPacket;
import ch.heig.network.packet.Packet;

public class ClientData {
    public ServerPlayer entity;
    public InetAddress address;
    public int port;
    public float lastUpdateClock=0;
    public int score=0;
    public boolean operator=true;


    public boolean checkIfPacketProvnence(Packet p){
        return p.inetAddress.equals(address) && p.port==port;
    }

    // to prevent command duplication, all command executed
    // return value is log on the client data so, if there resend
    // the same command id, it can send the output without calculation
    private List<String> _commandOutput = new ArrayList<>();

    public void logCommandOutput(String log){
        _commandOutput.add(log);
    }

    public String getCommandLog(int index){
        if(index > _commandOutput.size()-1 || index <0)return "";

        return _commandOutput.get(index);
    }
}
