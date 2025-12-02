package ch.heig.network.packet;

public class ExitPacket extends Packet{
    public String username;

    public ExitPacket(String username){
        this.username=username;
    }
}
