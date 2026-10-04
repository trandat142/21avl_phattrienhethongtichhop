package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai8;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.HexFormat;

public class FileServer {

    private static final int PORT = 5009;
    private static final Path UPLOAD_DIR = resolveUploadDir();

    private static Path resolveUploadDir() {
        // 1. Chay truc tiep tu file nguon: java .../FileServer.java
        try {
            var pd = FileServer.class.getProtectionDomain();
            var cs = (pd != null) ? pd.getCodeSource() : null;
            if (cs != null && cs.getLocation() != null && "file".equalsIgnoreCase(cs.getLocation().getProtocol())) {
                Path p = Path.of(cs.getLocation().toURI());
                if (Files.isRegularFile(p) && p.getFileName().toString().endsWith(".java")) {
                    return p.getParent().resolve("uploads");
                }
            }
        } catch (Exception ignored) {
        }

        // 2. Chay tu file .class da compile
        try {
            URL url = FileServer.class.getResource("FileServer.class");
            if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
                Path classFile = Path.of(url.toURI());
                return classFile.getParent().resolve("uploads");
            }
        } catch (Exception ignored) {
        }

        // 3. Neu terminal dang dung trong thu muc bai8
        Path currentDir = Path.of("").toAbsolutePath();
        if (currentDir.endsWith("bai8")) {
            return currentDir.resolve("uploads");
        }

        // 4. Tim thu muc bai8 bang cach duyet nguoc tu thu muc hien tai len
        Path targetDir = Path.of("thuchanh", "Lab_04_Java_Socket_TCP_UDP", "bai5_2", "bai8");
        Path check = currentDir;
        while (check != null) {
            Path candidate = check.resolve(targetDir);
            if (Files.isDirectory(candidate)) {
                return candidate.resolve("uploads");
            }
            check = check.getParent();
        }

        return targetDir.resolve("uploads");
    }

    public static void main(String[] args) {
        try {
            Files.createDirectories(UPLOAD_DIR);
        } catch (IOException e) {
            System.err.println("Khong tao duoc upload directory: " + e.getMessage());
            return;
        }

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("File server dang lang nghe cong " + PORT);
            System.out.println("Thu muc luu uploads: " + UPLOAD_DIR.toAbsolutePath());

            while (true) {
                try (Socket socket = server.accept()) {
                    serve(socket);
                } catch (IOException e) {
                    System.err.println("Loi phien file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Loi server: " + e.getMessage());
        }
    }

    private static void serve(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream())
        );

        DataOutputStream out = new DataOutputStream(
                socket.getOutputStream()
        );

        String fileName = in.readUTF();
        long fileSize = in.readLong();
        String expectedHash = in.readUTF();

        System.out.println("\n-------------------------------------------");
        System.out.println("[SERVER] Nhan yeu cau: " + fileName + " | Size: " + fileSize + " bytes");
        System.out.println("[SERVER] SHA-256 Client gui: " + expectedHash);

        if (!isSafeFileName(fileName)) {
            System.out.println("[SERVER] => TU CHOI: Ten file khong hop le hoac chua ../ (" + fileName + ")");
            out.writeUTF("ERR INVALID_FILENAME");
            out.flush();
            return;
        }

        if (fileSize < 0) {
            System.out.println("[SERVER] => TU CHOI: Kich thuoc am");
            out.writeUTF("ERR INVALID_SIZE");
            out.flush();
            return;
        }

        if (!expectedHash.matches("[0-9a-fA-F]{64}")) {
            System.out.println("[SERVER] => TU CHOI: Dinh dang hash sai");
            out.writeUTF("ERR INVALID_HASH");
            out.flush();
            return;
        }

        Path uploadDirAbs = UPLOAD_DIR.toAbsolutePath().normalize();
        Path target = uploadDirAbs.resolve(fileName).normalize();

        if (!target.getParent().equals(uploadDirAbs)) {
            System.out.println("[SERVER] => TU CHOI: Duong dan vuot khoi thu muc upload");
            out.writeUTF("ERR INVALID_FILENAME");
            out.flush();
            return;
        }

        out.writeUTF("READY");
        out.flush();
        System.out.println("[SERVER] San sang nhan du lieu...");

        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            out.writeUTF("ERR HASH_ALGORITHM");
            out.flush();
            return;
        }

        try (var fileOut = Files.newOutputStream(
                target,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            byte[] buffer = new byte[8192];
            long remaining = fileSize;

            while (remaining > 0) {
                int toRead = (int) Math.min(buffer.length, remaining);
                int count = in.read(buffer, 0, toRead);

                if (count == -1) {
                    throw new IOException("Client ket thuc truoc khi gui du byte");
                }

                fileOut.write(buffer, 0, count);
                digest.update(buffer, 0, count);
                remaining -= count;
            }
        }

        String actualHash = HexFormat.of().formatHex(digest.digest());
        System.out.println("[SERVER] SHA-256 Server tinh: " + actualHash);

        if (!actualHash.equalsIgnoreCase(expectedHash)) {
            Files.deleteIfExists(target);
            System.out.println("[SERVER] => THAT BAI: Hash khong khop! Da xoa file rac.");
            out.writeUTF("ERR HASH_MISMATCH");
        } else {
            System.out.println("[SERVER] => THANH CONG (OK): Da luu tai " + target);
            out.writeUTF("OK");
        }

        out.flush();
    }

    private static boolean isSafeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }

        if (fileName.contains("/")
                || fileName.contains("\\")
                || fileName.contains("..")) {
            return false;
        }

        Path path = Path.of(fileName);

        return path.getNameCount() == 1
                && path.getFileName().toString().equals(fileName);
    }
}
