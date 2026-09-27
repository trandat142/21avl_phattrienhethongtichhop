package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5005;

        try (
            Socket socket = new Socket(host, port);
            BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
            );
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                true
            )
        ) {
            System.out.print("Nickname: ");
            String nickname = console.readLine();

            if (nickname == null || nickname.isBlank()) {
                return;
            }

            // Doc dong WELCOME tu server truoc
            String welcome = in.readLine();
            System.out.println("Server: " + welcome);

            out.println("NICK " + nickname);

            String response = in.readLine();

            if (!"OK NICK".equals(response)) {
                System.out.println("Loi: " + response);
                return;
            }

            System.out.println("Dang nhap thanh cong! Xin chao " + nickname + "!");

            Thread receiver = new Thread(() -> {
                try {
                    String message;

                    while ((message = in.readLine()) != null) {
                        System.out.println();
                        System.out.println(message);
                        System.out.print("> ");
                    }
                } catch (IOException e) {
                    System.out.println("Mat ket noi toi server.");
                }
            });

            receiver.setDaemon(true);
            receiver.start();

            System.out.println("Lenh: USERS | MSG <noi dung> | QUIT");

            String command;

            while ((command = console.readLine()) != null) {
                out.println(command);

                if ("QUIT".equalsIgnoreCase(command.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Loi ket noi: " + e.getMessage());
        }
    }
}