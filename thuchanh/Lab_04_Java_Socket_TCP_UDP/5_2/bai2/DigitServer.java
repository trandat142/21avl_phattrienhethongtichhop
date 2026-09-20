import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitServer {

    private static final int PORT = 6000;

    private static final String[] DIGITS_VN = {
        "không", "một", "hai", "ba", "bốn",
        "năm", "sáu", "bảy", "tám", "chín"
    };

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("DigitServer đang lắng nghe trên port " + PORT);
            while (true) {
                try (Socket socket = server.accept()) {
                    System.out.println("Client kết nối: " + socket.getRemoteSocketAddress());
                    serve(socket);
                    System.out.println("Client ngắt kết nối.");
                } catch (IOException e) {
                    System.err.println("Lỗi phiên client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Không mở được server: " + e.getMessage());
        }
    }

    static void serve(Socket socket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                if (request.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                String response = process(request);
                out.println(response);
            }
        }
    }

    static String process(String request) {
        // Kiểm tra chuỗi rỗng
        if (request.isEmpty()) {
            return "ERR INVALID_DIGIT";
        }

        // Phải đúng 1 ký tự và là chữ số 0-9
        if (request.length() != 1 || !Character.isDigit(request.charAt(0))) {
            return "ERR INVALID_DIGIT";
        }

        int digit = request.charAt(0) - '0';
        return "OK " + DIGITS_VN[digit];
    }
}
