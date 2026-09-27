package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai6;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BenchmarkUdpServer {

    private static final int PORT = 5007;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP benchmark server: " + PORT);

            byte[] buffer = new byte[2048];

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                DatagramPacket response = new DatagramPacket(
                    request.getData(),
                    request.getLength(),
                    request.getAddress(),
                    request.getPort()
                );

                socket.send(response);
            }
        } catch (Exception e) {
            System.err.println("UDP server error: " + e.getMessage());
        }
    }
}
