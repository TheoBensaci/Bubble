package ch.heig.network.packet.data;

public class PlayerData extends EntityPacketData {
    public int id;
    public float positionX;
    public float positionY;
    public double rotation;
    public boolean isAlive;
    public boolean onDash;
    public int amo;
    public int numberOfDash;

    public PlayerData(float posX, float posY,double rotation,int id, boolean isAlive, boolean onDash, int amo, int numberOfDash){
        type = PacketDataType.Player;
        this.positionX=posX;
        this.positionY=posY;
        this.rotation=rotation;
        this.id=id;
        this.isAlive=isAlive;
        this.onDash=onDash;
        this.amo=amo;
        this.numberOfDash=numberOfDash;
    }
}
