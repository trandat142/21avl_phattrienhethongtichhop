import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeServer {

    private static final int TCP_PORT = 7000;
    private static final int UDP_PORT = 7001;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH mm ss");

    public static void main(String[] args) {
        // Chạy UDP server trên thread riêng
        Thread udpThread = new Thread(DateTimeServer::runUdpServer);
        udpThread.setDaemon(true);
        udpThread.start();

        // TCP server chạy trên main thread
        runTcpServer();
    }

    // ======================== TCP ========================

    static void runTcpServer() {
        try (ServerSocket server = new ServerSocket(TCP_PORT)) {
            System.out.println("TCP DateTimeServer đang lắng nghe trên port " + TCP_PORT);
            while (true) {
                try (Socket socket = server.accept()) {
                    System.out.println("[TCP] Client kết nối: " + socket.getRemoteSocketAddress());
                    serveTcp(socket);
                    System.out.println("[TCP] Client ngắt kết nối.");
                } catch (IOException e) {
                    System.err.println("[TCP] Lỗi phiên client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[TCP] Không mở được server: " + e.getMessage());
        }
    }

    static void serveTcp(Socket socket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                String response = processCommand(request.trim());
                out.println(response);
                if (request.trim().equalsIgnoreCase("QUIT")) break;
            }
        }
    }

    // ======================== UDP ========================

    static void runUdpServer() {
        try (DatagramSocket socket = new DatagramSocket(UDP_PORT)) {
            System.out.println("UDP DateTimeServer đang lắng nghe trên port " + UDP_PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket);

                String request = new String(requestPacket.getData(), 0,
                        requestPacket.getLength(), StandardCharsets.UTF_8).trim();
                System.out.println("[UDP] Nhận từ " + requestPacket.getSocketAddress() + ": " + request);

                String response = processCommand(request);
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

                DatagramPacket responsePacket = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        requestPacket.getAddress(), requestPacket.getPort());
                socket.send(responsePacket);
            }
        } catch (IOException e) {
            System.err.println("[UDP] Lỗi server: " + e.getMessage());
        }
    }

    // ======================== Xử lý lệnh chung ========================

    static String processCommand(String command) {
        if (command.equalsIgnoreCase("DATE")) {
            return "OK " + LocalDate.now().format(DATE_FMT);
        }
        if (command.equalsIgnoreCase("TIME")) {
            return "OK " + LocalTime.now().format(TIME_FMT);
        }
        if (command.equalsIgnoreCase("DATETIME")) {
            return "OK " + LocalDate.now().format(DATE_FMT) + " " + LocalTime.now().format(TIME_FMT);
        }
        if (command.equalsIgnoreCase("QUIT")) {
            return "OK BYE";
        }
        return "ERR UNKNOWN_COMMAND";
    }
}
