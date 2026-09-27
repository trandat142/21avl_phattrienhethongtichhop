package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai9;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class MulticastSender {

    private static final String GROUP = "239.255.0.1";
    private static final int PORT = 5010;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println(
                    "Dung: java multicast.MulticastSender <message>"
            );
            return;
        }

        String message = String.join(" ", args);

        InetAddress group = InetAddress.getByName(GROUP);
        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length,
                    group,
                    PORT
            );

            socket.send(packet);

            System.out.println(
                    "Da gui multicast toi "
                    + GROUP + ":" + PORT
            );
        }
    }
}
