package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BenchmarkTcpServer {

    private static final int PORT = 5006;
    private static final int MESSAGE_SIZE = 256;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("TCP benchmark server: " + PORT);

            while (true) {
                try (
                    Socket socket = server.accept();
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream())
                ) {
                    byte[] buffer = new byte[MESSAGE_SIZE];

                    while (true) {
                        try {
                            in.readFully(buffer);
                            out.write(buffer);
                            out.flush();
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("TCP server error: " + e.getMessage());
        }
    }
}
