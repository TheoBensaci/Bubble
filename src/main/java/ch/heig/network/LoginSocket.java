/**
 *   Autheur: Theo Bensaci
 *   Date: 18:54 13.11.2025
 *   Description: socket use to manage login in to server
 */

package ch.heig.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import ch.heig.network.packet.LoginPacket;
import ch.heig.network.packet.Packet;
import ch.heig.network.packet.PacketType;

public record LoginSocket(InetAddress serverAddress, int serverPort, int port) {


    /**
     * Try to login to the sever
     * @return loging packet accepted
     */
    public LoginPacket login() {
        LoginPacket lp;

        // ask to join a game
        try {
            DatagramSocket loginSocket = new DatagramSocket(this.port);

            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, this.serverAddress, this.serverPort);

                lp = new LoginPacket();
                lp.username = generateUsername();

                datagram.setData(lp.serialize());
                loginSocket.send(datagram);

                System.out.println("Try to login with '" + lp.username + "'");

                buffer = new byte[1000];
                datagram = new DatagramPacket(buffer, buffer.length);
                loginSocket.receive(datagram);

                Packet p = Packet.unserialize(buffer);
                if (p == null || p.type != PacketType.login) {
                    System.out.println("Unknow respond packet");
                    continue;
                }

                lp = (LoginPacket) p;
                if (lp.id < 0) {
                    System.out.println("Username : '" + lp.username + "' all ready taken...");
                    continue;
                }

                System.out.println("Succefully login with username '" + lp.username + "'");
                break;
            }

            loginSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lp;
    }

    /**
     * generate username
     * @return
     */
    private String generateUsername() {
        char[] usernameChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890?.,-_:;<>*/+%&=¦@#°§¬|¢()[]$!".toCharArray();


        StringBuilder sb = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < 15; i++) {
            int index = rand.nextInt(0, usernameChar.length);
            sb.append(usernameChar[index]);
        }
        return sb.toString();
    }

}
