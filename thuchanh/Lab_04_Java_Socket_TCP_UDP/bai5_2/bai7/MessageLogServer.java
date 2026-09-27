package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageLogServer {

    private static final int PORT = 5008;
    private static final Path LOG_DIR = resolveLogDir();

    private static Path resolveLogDir() {
        try {
            URL url = MessageLogServer.class.getResource("MessageLogServer.class");
            if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
                Path classFile = Path.of(url.toURI());
                return classFile.getParent().resolve("data").resolve("logs");
            }
        } catch (Exception ignored) {
        }
        return Path.of("thuchanh", "Lab_04_Java_Socket_TCP_UDP", "bai5_2", "bai7", "data", "logs");
    }

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            System.err.println("Khong tao duoc thu muc log: " + e.getMessage());
            return;
        }

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Message log server dang lang nghe cong " + PORT);
            System.out.println("Thu muc luu log: " + LOG_DIR.toAbsolutePath());

            while (true) {
                try (Socket socket = server.accept()) {
                    serve(socket);
                } catch (IOException e) {
                    System.err.println("Loi phien client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Loi server: " + e.getMessage());
        }
    }

    private static void serve(Socket socket) throws IOException {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                true
            )
        ) {
            String hello = in.readLine();

            if (hello == null || !hello.startsWith("HELLO ")) {
                out.println("ERR HELLO_REQUIRED");
                return;
            }

            String clientId = hello.substring(6).trim();

            if (!clientId.matches("[A-Za-z0-9_-]+")) {
                out.println("ERR INVALID_CLIENT_ID");
                return;
            }

            Path logFile = LOG_DIR.resolve(clientId + ".txt");

            out.println("OK HELLO");

            String message;

            while ((message = in.readLine()) != null) {
                if ("QUIT".equalsIgnoreCase(message.trim())) {
                    out.println("OK BYE");
                    break;
                }

                String timestamp = LocalDateTime.now().format(TIME_FORMAT);
                String remote = String.valueOf(socket.getRemoteSocketAddress());

                String logLine = timestamp + " | " + remote + " | " + message;

                try (BufferedWriter writer = Files.newBufferedWriter(
                        logFile,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                )) {
                    writer.write(logLine);
                    writer.newLine();
                }

                System.out.println("Da ghi log: " + logFile.toAbsolutePath());
                out.println("OK SAVED");
            }
        }
    }
}
