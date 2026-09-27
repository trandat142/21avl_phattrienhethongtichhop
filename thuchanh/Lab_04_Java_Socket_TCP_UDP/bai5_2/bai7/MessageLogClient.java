package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MessageLogClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5008;

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
            System.out.print("clientId: ");
            String clientId = console.readLine();

            out.println("HELLO " + clientId);

            String helloResponse = in.readLine();
            System.out.println("Server: " + helloResponse);

            if (!"OK HELLO".equals(helloResponse)) {
                return;
            }

            System.out.println("Nhap noi dung. QUIT de thoat.");

            String message;

            while ((message = console.readLine()) != null) {
                out.println(message);

                String response = in.readLine();

                if (response == null) {
                    break;
                }

                System.out.println("Server: " + response);

                if ("QUIT".equalsIgnoreCase(message.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Loi client: " + e.getMessage());
        }
    }
}
