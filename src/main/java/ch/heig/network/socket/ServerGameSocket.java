/**
 *   Autheur: Theo Bensaci
 *   Date: 07:54 26.11.2025
 *   Description: Class use to add behavior to the game socket specific for the client, for example, early packet drop
 */

package ch.heig.network.socket;

import ch.heig.core.Entity;
import ch.heig.network.ClientData;
import ch.heig.network.coreVariant.ServerGame;
import ch.heig.network.packet.CommandPacket;
import ch.heig.network.packet.LoginPacket;
import ch.heig.network.packet.Packet;
import ch.heig.network.packet.PacketType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerGameSocket extends GameSocket{

    private ServerGame _game;


    public ServerGameSocket(){
        super();
    }

    public ServerGameSocket(int listenDefaultPort){
        super(listenDefaultPort);
    }


    @Override
    protected boolean packetPreProcess(Packet packet) {
        switch(packet.type){
            case PacketType.command:
                CommandPacket cp = packet.safeCast(CommandPacket.class);
                if(cp==null)return false;

                // check client permition
                ClientData cd = _game.serverPlayers.get(cp.username);
                if(cd==null || !cd.checkIfPacketProvnence(packet)){
                    cp.arg="ERROR MESSAGE";
                    cp.commandType= CommandPacket.Command.error;
                    send(cp,packet.inetAddress,packet.port);
                    return false;
                }

                // if the command as all ready
                String log = cd.getCommandLog(cp.index);
                if(!log.isEmpty()){
                    cp.arg=log;
                    cp.commandType= CommandPacket.Command.log;
                    send(cp,packet.inetAddress,packet.port);
                    return false;
                }

                // help command
                if(cp.commandType== CommandPacket.Command.help){
                    cp.arg=cd.operator?CommandPacket.CommandOpHelp:CommandPacket.CommandHelp;
                    cp.commandType= CommandPacket.Command.log;
                    send(cp,packet.inetAddress,packet.port);
                    return false;
                }

                // player list command
                if(cp.commandType== CommandPacket.Command.players){
                    Set<Map.Entry<String,ClientData>> plList = _game.serverPlayers.entrySet();
                    StringBuilder str = new StringBuilder("players : \n");
                    for (Map.Entry<String,ClientData> p : plList){
                        str.append("\t- ").append(p.getKey()).append((p.getValue().operator)?" [OP]":"").append('\n');
                    }
                    cp.arg=str.toString();
                    cp.commandType= CommandPacket.Command.log;
                    send(cp,packet.inetAddress,packet.port);
                    return false;
                }

                if(!cd.operator)return false;

                // the majority of the thoses command need to be treated in the main game loop du to concurency
                return true;

            case PacketType.login :
                LoginPacket lp = packet.safeCast(LoginPacket.class);
                if(lp==null)return false;

                if(_game.serverPlayers.containsKey(lp.username)  || lp.username.contains(" ")){
                    lp.id=-1;
                    send(lp,packet.inetAddress,packet.port);
                    return false;
                }

                Entity e = _game.createNewPlayer(lp.username,lp.playerColor,packet.inetAddress,packet.port);
                lp.id=e.getId();
                send(lp,packet.inetAddress,packet.port);
                return false;

            case PacketType.ping:

                // if receve -> send it back
                send(packet,packet.inetAddress,packet.port);
                return false;

            default:
                return super.packetPreProcess(packet);
        }
    }

    public void setGame(ServerGame game){
        _game=game;
    }

}
