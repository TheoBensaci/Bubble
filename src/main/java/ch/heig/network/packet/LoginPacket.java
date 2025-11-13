package ch.heig.network.packet;

public class LoginPacket extends Packet {
    public String username="";              // username valid or ask depending on who send this packet
    public int id=0;                        // if id < 0 -> username not available, else ok
    public LoginPacket(){
        type=PacketType.login;
    }
}
