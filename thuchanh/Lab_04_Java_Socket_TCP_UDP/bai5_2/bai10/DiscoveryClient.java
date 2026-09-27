package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai10;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class DiscoveryClient {

    private static final String GROUP = "239.255.0.1";
    private static final int DISCOVERY_PORT = 5011;
    private static final int TIMEOUT_MS = 3000;

    public static void main(String[] args) throws Exception {
        System.out.println("Dang tim kiem service...");

        Set<String> seen = new HashSet<>();

        InetAddress group = InetAddress.getByName(GROUP);
        byte[] message = "DISCOVER_SERVICE".getBytes(StandardCharsets.UTF_8);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);

            DatagramPacket request = new DatagramPacket(
                    message,
                    message.length,
                    group,
                    DISCOVERY_PORT
            );

            socket.send(request);

            byte[] buffer = new byte[512];

            while (true) {
                DatagramPacket response =
                        new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(response);
                } catch (SocketTimeoutException e) {
                    break;
                }

                String reply = new String(
                        response.getData(),
                        0,
                        response.getLength(),
                        StandardCharsets.UTF_8
                );

                String serverAddress =
                        response.getAddress().getHostAddress();

                String key = serverAddress + ":" + reply;

                if (seen.contains(key)) {
                    continue;
                }

                seen.add(key);

                System.out.println(
                        "Tim thay tu " + serverAddress + ": " + reply
                );

                if (reply.startsWith("SERVICE ")) {
                    String[] parts = reply.split("\\s+");

                    if (parts.length >= 3) {
                        String serviceName = parts[1];
                        int tcpPort = Integer.parseInt(parts[2]);
                        String version = parts.length >= 4 ? parts[3] : "?";

                        System.out.println(
                                "Ket noi TCP toi "
                                + serverAddress + ":" + tcpPort
                        );

                        try (
                            Socket tcp = new Socket(
                                    serverAddress, tcpPort
                            );
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                            tcp.getInputStream(),
                                            StandardCharsets.UTF_8
                                    )
                            );
                            PrintWriter out = new PrintWriter(
                                    new OutputStreamWriter(
                                            tcp.getOutputStream(),
                                            StandardCharsets.UTF_8
                                    ),
                                    true
                            )
                        ) {
                            out.println("PING");
                            String pong = in.readLine();
                            System.out.println("TCP response: " + pong);
                        }
                    }
                }
            }
        }

        if (seen.isEmpty()) {
            System.out.println("Khong tim thay service nao.");
        }
    }
}
