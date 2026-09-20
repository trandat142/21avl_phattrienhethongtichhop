import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostUriInspector {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java HostUriInspector <hostname> <URI>");
            System.out.println("Ví dụ: java HostUriInspector google.com https://example.com:8080/path?q=1#frag");
            return;
        }

        String hostname = args[0];
        String uriString = args[1];

        // --- Phần 1: Host Inspector ---
        System.out.println("=== HOST INSPECTOR ===");
        System.out.println("Hostname: " + hostname);
        inspectHost(hostname);

        System.out.println();

        // --- Phần 2: URI Inspector ---
        System.out.println("=== URI INSPECTOR ===");
        System.out.println("URI gốc: " + uriString);
        inspectUri(uriString);
    }

    static void inspectHost(String hostname) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            System.out.println("Số lượng IP: " + addresses.length);
            System.out.println();

            for (int i = 0; i < addresses.length; i++) {
                InetAddress addr = addresses[i];
                String ip = addr.getHostAddress();

                String type;
                if (addr instanceof Inet4Address) {
                    type = "IPv4";
                } else if (addr instanceof Inet6Address) {
                    type = "IPv6";
                } else {
                    type = "Unknown";
                }

                System.out.println("  [" + (i + 1) + "] IP: " + ip);
                System.out.println("      Loại: " + type);
                System.out.println("      Loopback: " + addr.isLoopbackAddress());
                System.out.println("      Site-local: " + addr.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("Lỗi: Không phân giải được hostname '" + hostname + "'");
        }
    }

    static void inspectUri(String uriString) {
        try {
            URI uri = new URI(uriString);

            System.out.println("  Scheme:   " + (uri.getScheme() != null ? uri.getScheme() : "(không có)"));
            System.out.println("  Host:     " + (uri.getHost() != null ? uri.getHost() : "(không có)"));
            System.out.println("  Port:     " + (uri.getPort() != -1 ? uri.getPort() : "(không chỉ định)"));
            System.out.println("  Path:     " + (uri.getPath() != null && !uri.getPath().isEmpty() ? uri.getPath() : "(không có)"));
            System.out.println("  Query:    " + (uri.getQuery() != null ? uri.getQuery() : "(không có)"));
            System.out.println("  Fragment: " + (uri.getFragment() != null ? uri.getFragment() : "(không có)"));
        } catch (URISyntaxException e) {
            System.err.println("Lỗi: URI không hợp lệ - " + e.getMessage());
        }
    }
}
