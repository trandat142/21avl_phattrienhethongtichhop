package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai4;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5004;

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
            System.out.println("Da ket noi toi " + host + ":" + port);
            System.out.println("Vi du: CALC + 100 200");
            System.out.println("Nhap QUIT de thoat.");

            String request;

            while ((request = console.readLine()) != null) {
                out.println(request);

                String response = in.readLine();

                if (response == null) {
                    System.out.println("Server da dong ket noi.");
                    break;
                }

                System.out.println("Server: " + response);

                if ("QUIT".equalsIgnoreCase(request.trim())) {
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Port phai la so nguyen.");
        } catch (IOException e) {
            System.err.println("Loi ket noi: " + e.getMessage());
        }
    }
}