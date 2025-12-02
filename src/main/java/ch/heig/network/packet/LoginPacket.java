/**
 *   Autheur: Theo Bensaci
 *   Date: 18:18 13.11.2025
 *   Description: Packet use to log a client
 */

package ch.heig.network.packet;

public class LoginPacket extends Packet {
    public String username="";              // username valid or ask depending on who send this packet

    // Use to tell which entity id the player is linked, it's also use in case of error
    // if the username is invalid, the id will be < 0 to indicated that
    // if the username is valid, the id will be entity id of player on the server
    public int id=0;
    public int playerColor=0;               // selected player color

    public LoginPacket(String username, int playerColor){
        type=PacketType.login;
        this.username=username;
        this.playerColor=playerColor;
    }
}
