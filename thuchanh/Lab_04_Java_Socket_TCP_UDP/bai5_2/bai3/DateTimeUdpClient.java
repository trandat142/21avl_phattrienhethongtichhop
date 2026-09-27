import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class DateTimeUdpClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 7001;

        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            socket.setSoTimeout(3000); // timeout 3 giây
            InetAddress serverAddr = InetAddress.getByName(host);

            System.out.println("UDP Client - gửi tới " + host + ":" + port);
            System.out.println("Lệnh: DATE, TIME, DATETIME (không cần QUIT cho UDP)");

            String input;
            while (true) {
                System.out.print(">> ");
                input = console.readLine();
                if (input == null || input.trim().equalsIgnoreCase("QUIT")) {
                    System.out.println("Thoát client.");
                    break;
                }

                // Gửi request
                byte[] sendData = input.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length, serverAddr, port);
                socket.send(sendPacket);

                // Nhận response
                byte[] recvBuffer = new byte[1024];
                DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
                try {
                    socket.receive(recvPacket);
                    String response = new String(recvPacket.getData(), 0,
                            recvPacket.getLength(), StandardCharsets.UTF_8);
                    System.out.println("Server: " + response);
                } catch (SocketTimeoutException e) {
                    System.out.println("Không nhận được phản hồi (timeout).");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Port phải là số nguyên.");
        } catch (IOException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }
}
