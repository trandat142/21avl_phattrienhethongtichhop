package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcServer {

    private static final int PORT = 5004;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Calc TCP server dang lang nghe cong " + PORT);

            while (true) {
                try (Socket socket = server.accept()) {
                    serve(socket);
                } catch (IOException e) {
                    System.err.println("Loi phien client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Khong mo duoc server: " + e.getMessage());
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
            String request;

            while ((request = in.readLine()) != null) {
                String response = process(request);
                out.println(response);

                if ("QUIT".equalsIgnoreCase(request.trim())) {
                    break;
                }
            }
        }
    }

    static String process(String request) {
        if ("QUIT".equalsIgnoreCase(request.trim())) {
            return "OK BYE";
        }

        String[] parts = request.trim().split("\\s+");

        if (parts.length != 4 || !"CALC".equalsIgnoreCase(parts[0])) {
            return "ERR INVALID_FORMAT";
        }

        String operator = parts[1];

        if (!operator.equals("+")
                && !operator.equals("-")
                && !operator.equals("*")
                && !operator.equals("/")) {
            return "ERR UNSUPPORTED_OPERATOR";
        }

        long a;
        long b;

        try {
            a = Long.parseLong(parts[2]);
            b = Long.parseLong(parts[3]);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }

        try {
            long result;

            switch (operator) {
                case "+":
                    result = Math.addExact(a, b);
                    break;
                case "-":
                    result = Math.subtractExact(a, b);
                    break;
                case "*":
                    result = Math.multiplyExact(a, b);
                    break;
                case "/":
                    if (b == 0) {
                        return "ERR DIVIDE_BY_ZERO";
                    }
                    result = a / b;
                    break;
                default:
                    return "ERR UNSUPPORTED_OPERATOR";
            }

            return "OK " + result;

        } catch (ArithmeticException e) {
            return "ERR OVERFLOW";
        }
    }
}