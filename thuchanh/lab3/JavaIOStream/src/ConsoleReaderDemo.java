import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleReaderDemo {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        int count = 0;
        System.out.println("Nhập văn bản; nhập q để kết thúc:");
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null || line.equalsIgnoreCase("q")) {
                    break;
                }
                count++;
                System.out.printf("Dòng %d: %s%n", count, line);
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc dữ liệu: " + e.getMessage());
        }
        System.out.println("Tổng số dòng đã nhập: " + count);
    }
}

//======================= Cau hoi suy luan =======================//

//1. Vì sao cần InputStreamReader giữa System.in và BufferedReader?
//2. readLine() trả về giá trị nào khi gặp EOF?
//3. Vì sao ví dụ không đóng reader gắn với System.in?
