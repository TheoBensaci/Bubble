package com.metheo.network.packet;

import java.io.*;

public abstract class Packet implements Serializable {
    public PacketType type;
    public byte[] serialize(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(this);
            out.flush();
            return bos.toByteArray();
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    public static Packet unserialize(byte[] data){
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try (ObjectInput in = new ObjectInputStream(bis)) {
            return (Packet) in.readObject();
        } catch (Exception ex) {
            return null;
        }
    }
}
