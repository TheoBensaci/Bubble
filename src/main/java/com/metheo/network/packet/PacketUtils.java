package com.metheo.network.packet;

import java.io.*;

public class PacketUtils {
    public static <E extends Serializable> byte[] serialize(E data){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(data);
            out.flush();
            return bos.toByteArray();
        } catch (Exception ex) {
            return new byte[0];
        }
    }

    public static <E extends Serializable> E unSerialize(byte[] data){
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try (ObjectInput in = new ObjectInputStream(bis)) {
            return (E) in.readObject();
        } catch (Exception ex) {
            return null;
        }
    }
}
