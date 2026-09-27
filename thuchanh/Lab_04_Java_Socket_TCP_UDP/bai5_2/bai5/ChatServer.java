package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static final int PORT = 5005;
    private static final ExecutorService POOL = Executors.newFixedThreadPool(20);

    // key = nickname, value = writer cua client.
    private static final Map<String, PrintWriter> CLIENTS =
            new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Chat TCP server dang lang nghe cong " + PORT);

            while (true) {
                Socket socket = server.accept();
                POOL.execute(() -> handleClient(socket));
            }
        } catch (IOException e) {
            System.err.println("Loi server: " + e.getMessage());
        } finally {
            POOL.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String nickname = null;
        boolean registered = false;

        try (
            Socket client = socket;
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8)
            );
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8),
                true
            )
        ) {
            out.println("WELCOME");

            String first = in.readLine();

            if (first == null || !first.startsWith("NICK ")) {
                out.println("ERR NICK_REQUIRED");
                return;
            }

            nickname = first.substring(5).trim();

            if (!nickname.matches("[A-Za-z0-9_-]+")) {
                out.println("ERR INVALID_NICK");
                return;
            }

            if (CLIENTS.putIfAbsent(nickname, out) != null) {
                out.println("ERR NICK_TAKEN");
                return;
            }

            registered = true;
            out.println("OK NICK");
            System.out.println("[+] " + nickname + " connected from "
                    + client.getRemoteSocketAddress());

            String request;

            while ((request = in.readLine()) != null) {
                if ("USERS".equalsIgnoreCase(request.trim())) {
                    sendUsers(out);
                    continue;
                }

                if ("QUIT".equalsIgnoreCase(request.trim())) {
                    out.println("OK BYE");
                    break;
                }

                if (request.startsWith("MSG ")) {
                    String message = request.substring(4);

                    if (message.isBlank()) {
                        out.println("ERR EMPTY_MESSAGE");
                        continue;
                    }

                    broadcast(nickname, message);
                    out.println("OK SENT");
                    continue;
                }

                out.println("ERR UNKNOWN_COMMAND");
            }

        } catch (IOException e) {
            System.err.println("[!] Client " + nickname
                    + " loi/ngat ket noi: " + e.getMessage());
        } finally {
            if (registered) {
                CLIENTS.remove(nickname);
                System.out.println("[-] " + nickname + " disconnected.");
            }
        }
    }

    private static void sendUsers(PrintWriter out) {
        List<String> names = new ArrayList<>(CLIENTS.keySet());
        Collections.sort(names);
        out.println("USERS " + String.join(",", names));
    }

    private static void broadcast(String sender, String message) {
        String payload = "FROM " + sender + ": " + message;

        CLIENTS.forEach((nickname, writer) -> {
            if (!nickname.equals(sender)) {
                writer.println(payload);
            }
        });
    }
}