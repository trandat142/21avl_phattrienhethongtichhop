package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai8;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;

public class FileClient {

    private static final int PORT = 5009;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Cach dung: java FileClient <file> [customFileName] [fakeHash: true/false]");
            System.err.println("  1. Gui binh thuong : java FileClient demo.txt");
            System.err.println("  2. Test chuoi ../  : java FileClient demo.txt ../hack.txt");
            System.err.println("  3. Test sai hash   : java FileClient demo.txt demo.txt fake");
            return;
        }

        Path file = Path.of(args[0]);
        String customFileName = (args.length >= 2) ? args[1] : null;
        boolean fakeHash = (args.length >= 3 && "fake".equalsIgnoreCase(args[2]));

        try {
            sendFile(file, customFileName, fakeHash);
        } catch (IOException e) {
            System.err.println("Loi gui file: " + e.getMessage());
        }
    }

    private static void sendFile(Path file, String customFileName, boolean fakeHash) throws IOException {
        if (!Files.isRegularFile(file)) {
            throw new IOException("File khong ton tai hoac khong phai file thuong.");
        }

        String fileName = (customFileName != null) ? customFileName : file.getFileName().toString();
        long fileSize = Files.size(file);
        String hash = fakeHash ? "0".repeat(64) : sha256(file);

        System.out.println("[CLIENT] Gui file: " + fileName + " (" + fileSize + " bytes)");
        System.out.println("[CLIENT] SHA-256 : " + hash);

        try (
            Socket socket = new Socket("localhost", PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream())
            )
        ) {
            out.writeUTF(fileName);
            out.writeLong(fileSize);
            out.writeUTF(hash);
            out.flush();

            String ready = in.readUTF();

            if (!"READY".equals(ready)) {
                System.out.println("[CLIENT] Server tu choi nhan: " + ready);
                return;
            }

            try (var input = Files.newInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;

                while ((count = input.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }

                out.flush();
            }

            String result = in.readUTF();
            System.out.println("[CLIENT] Phan hoi tu Server: " + result);
        }
    }

    private static String sha256(Path file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            try (var input = Files.newInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;

                while ((count = input.read(buffer)) != -1) {
                    digest.update(buffer, 0, count);
                }
            }

            return HexFormat.of().formatHex(digest.digest());

        } catch (Exception e) {
            throw new IOException("Khong tinh duoc SHA-256", e);
        }
    }
}
