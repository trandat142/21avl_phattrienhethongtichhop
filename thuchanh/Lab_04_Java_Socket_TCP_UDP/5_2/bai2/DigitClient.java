import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 6000;

        try (Socket socket = new Socket(host, port);
             BufferedReader console = new BufferedReader(
                 new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(new InputStreamReader(
                 socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                 socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("Đã kết nối tới server.");
            System.out.println("Nhập một chữ số (0-9) để đổi sang tiếng Việt, QUIT để thoát.");

            String input;
            while (true) {
                System.out.print(">> ");
                input = console.readLine();
                if (input == null) break;

                out.println(input);

                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server đã đóng kết nối.");
                    break;
                }
                System.out.println("Server: " + response);

                if (input.equalsIgnoreCase("QUIT")) break;
            }
        } catch (NumberFormatException e) {
            System.err.println("Port phải là số nguyên.");
        } catch (IOException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}
