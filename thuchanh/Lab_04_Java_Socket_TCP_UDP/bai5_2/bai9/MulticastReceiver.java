package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai9;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

public class MulticastReceiver {

    private static final String GROUP = "239.255.0.1";
    private static final int PORT = 5010;

    public static void main(String[] args) throws Exception {
        String interfaceName = args.length > 0 ? args[0] : null;

        NetworkInterface networkInterface =
                findInterface(interfaceName);

        if (networkInterface == null) {
            throw new IllegalStateException(
                    "Khong tim thay NetworkInterface phu hop."
            );
        }

        InetAddress group = InetAddress.getByName(GROUP);

        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            socket.setReuseAddress(true);

            InetSocketAddress groupAddress =
                    new InetSocketAddress(group, PORT);

            socket.joinGroup(groupAddress, networkInterface);

            System.out.println(
                    "Receiver joined " + GROUP + ":" + PORT
                    + " via " + networkInterface.getName()
            );

            byte[] buffer = new byte[2048];

            try {
                while (true) {
                    DatagramPacket packet =
                            new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    String message = new String(
                            packet.getData(),
                            0,
                            packet.getLength(),
                            StandardCharsets.UTF_8
                    );

                    System.out.println(
                            "Nhan tu "
                            + packet.getAddress().getHostAddress()
                            + ": "
                            + message
                    );
                }
            } finally {
                socket.leaveGroup(groupAddress, networkInterface);
                System.out.println("Da leave multicast group.");
            }
        }
    }

    private static NetworkInterface findInterface(String name)
            throws Exception {

        if (name != null && !name.isBlank()) {
            NetworkInterface ni = NetworkInterface.getByName(name);

            if (ni == null || !ni.isUp()) {
                throw new IllegalArgumentException(
                        "NetworkInterface khong hop le: " + name
                );
            }

            return ni;
        }

        var interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (ni.isUp()
                    && !ni.isLoopback()
                    && ni.supportsMulticast()) {
                return ni;
            }
        }

        return null;
    }
}
