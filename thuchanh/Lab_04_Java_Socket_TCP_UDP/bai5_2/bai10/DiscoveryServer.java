package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai10;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscoveryServer {

    private static final String GROUP = "239.255.0.1";
    private static final int DISCOVERY_PORT = 5011;
    private static final int TCP_PORT = 5012;

    private static final String SERVICE_NAME = "DemoService";
    private static final String VERSION = "1.0";

    private static final ExecutorService POOL =
            Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        Thread discoveryThread = new Thread(
                DiscoveryServer::runDiscovery,
                "discovery-udp"
        );

        Thread tcpThread = new Thread(
                DiscoveryServer::runTcpService,
                "service-tcp"
        );

        discoveryThread.start();
        tcpThread.start();

        System.out.println(
                "Discovery server: UDP " + DISCOVERY_PORT
                + ", TCP " + TCP_PORT
        );
    }

    private static void runDiscovery() {
        try (MulticastSocket socket =
                     new MulticastSocket(DISCOVERY_PORT)) {

            socket.setReuseAddress(true);

            InetAddress group = InetAddress.getByName(GROUP);
            NetworkInterface networkInterface = findInterface();

            if (networkInterface == null) {
                throw new IllegalStateException(
                        "Khong tim thay NetworkInterface multicast."
                );
            }

            InetSocketAddress groupAddress = new InetSocketAddress(
                    group,
                    DISCOVERY_PORT
            );

            socket.joinGroup(groupAddress, networkInterface);

            System.out.println(
                    "UDP discovery joined " + GROUP
                    + " via " + networkInterface.getName()
            );

            byte[] buffer = new byte[512];

            try {
                while (true) {
                    DatagramPacket request =
                            new DatagramPacket(buffer, buffer.length);

                    socket.receive(request);

                    String message = new String(
                            request.getData(),
                            0,
                            request.getLength(),
                            StandardCharsets.UTF_8
                    );

                    if (!"DISCOVER_SERVICE".equals(message.trim())) {
                        continue;
                    }

                    String response =
                            "SERVICE "
                            + SERVICE_NAME + " "
                            + TCP_PORT + " "
                            + VERSION;

                    byte[] data =
                            response.getBytes(StandardCharsets.UTF_8);

                    DatagramPacket reply = new DatagramPacket(
                            data,
                            data.length,
                            request.getAddress(),
                            request.getPort()
                    );

                    socket.send(reply);
                }
            } finally {
                socket.leaveGroup(groupAddress, networkInterface);
            }

        } catch (Exception e) {
            System.err.println("Discovery error: " + e.getMessage());
        }
    }

    private static NetworkInterface findInterface()
            throws Exception {

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

    private static void runTcpService() {
        try (ServerSocket server = new ServerSocket(TCP_PORT)) {
            while (true) {
                Socket socket = server.accept();
                POOL.execute(() -> serve(socket));
            }
        } catch (Exception e) {
            System.err.println("TCP service error: " + e.getMessage());
        }
    }

    private static void serve(Socket socket) {
        try (
            Socket client = socket;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            );
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(
                            client.getOutputStream(),
                            StandardCharsets.UTF_8
                    ),
                    true
            )
        ) {
            String request = in.readLine();

            if ("PING".equalsIgnoreCase(request)) {
                out.println(
                        "PONG "
                        + SERVICE_NAME + " "
                        + VERSION
                );
            } else {
                out.println("ERR UNKNOWN_COMMAND");
            }

        } catch (Exception e) {
            System.err.println("Client TCP error: " + e.getMessage());
        }
    }
}
