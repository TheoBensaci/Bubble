/**
 *   Autheur: Theo Bensaci
 *   Date: 18:18 13.11.2025
 *   Description: Packet use to log a client
 */

package ch.heig.network.packet;

public class LoginPacket extends Packet {
    public String username="";              // username valid or ask depending on who send this packet
    public int id=0;                        // if id < 0 -> username not available, else ok
    public int playerColor=0;
    public LoginPacket(){
        type=PacketType.login;
    }

    public LoginPacket(String username, int playerColor){
        this();
        this.username=username;
        this.playerColor=playerColor;
    }
}
