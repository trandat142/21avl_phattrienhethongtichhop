import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoServer {
    public final static int serverPort = 9000;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(serverPort);
            System.out.println("Server da duoc tao");

            while (true) {
                try {
                    Socket s = ss.accept();
                    System.out.println("Client da ket noi");

                    InputStream is = s.getInputStream();
                    OutputStream os = s.getOutputStream();

                    int ch;
                    while (true) {
                        ch = is.read();
                        if (ch == -1) break;
                        System.out.println("Server nhan duoc: " + (char) ch);
                        os.write(ch);
                    }

                    s.close();
                } catch (IOException ie1) {
                    System.out.println("Connection Error: " + ie1);
                }
            }
        } catch (IOException ie) {
            System.out.println("Server Creation Error: " + ie);
        }
    }
}
