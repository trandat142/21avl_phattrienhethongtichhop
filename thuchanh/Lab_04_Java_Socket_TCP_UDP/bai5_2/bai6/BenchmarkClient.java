package thuchanh.Lab_04_Java_Socket_TCP_UDP.bai5_2.bai6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class BenchmarkClient {

    private static final String HOST = "127.0.0.1";
    private static final int TCP_PORT = 5006;
    private static final int UDP_PORT = 5007;

    private static final int MESSAGES = 1000;
    private static final int MESSAGE_SIZE = 256;
    private static final int RUNS = 5;
    private static final int TIMEOUT_MS = 2000;

    public static void main(String[] args) throws Exception {
        byte[] payload = createPayload();

        System.out.println("=== TCP ===");
        for (int run = 1; run <= RUNS; run++) {
            Result result = runTcp(payload);
            printResult("TCP", run, result);
        }

        System.out.println();
        System.out.println("=== UDP ===");
        for (int run = 1; run <= RUNS; run++) {
            Result result = runUdp(payload);
            printResult("UDP", run, result);
        }

        System.out.println();
        System.out.println("Khong ket luan giao thuc nao luon nhanh hon chi tu mot lan chay.");
    }

    private static Result runTcp(byte[] payload) throws Exception {
        int received = 0;
        byte[] buffer = new byte[MESSAGE_SIZE];

        long start = System.nanoTime();

        try (Socket socket = new Socket(HOST, TCP_PORT)) {
            socket.setSoTimeout(TIMEOUT_MS);

            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());
            DataInputStream in =
                    new DataInputStream(socket.getInputStream());

            for (int i = 0; i < MESSAGES; i++) {
                out.write(payload);
                out.flush();

                try {
                    in.readFully(buffer);
                    received++;
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
        }

        long elapsed = System.nanoTime() - start;
        return new Result(elapsed, received);
    }

    private static Result runUdp(byte[] payload) throws Exception {
        int received = 0;

        InetAddress address = InetAddress.getByName(HOST);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);

            long start = System.nanoTime();

            for (int i = 0; i < MESSAGES; i++) {
                DatagramPacket request = new DatagramPacket(
                    payload, payload.length, address, UDP_PORT
                );

                socket.send(request);

                byte[] buffer = new byte[MESSAGE_SIZE];
                DatagramPacket response =
                        new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(response);
                    if (response.getLength() == MESSAGE_SIZE) {
                        received++;
                    }
                } catch (SocketTimeoutException e) {
                }
            }

            long elapsed = System.nanoTime() - start;
            return new Result(elapsed, received);
        }
    }

    private static byte[] createPayload() {
        byte[] payload = new byte[MESSAGE_SIZE];
        byte[] text = "JAVA-SOCKET-BENCHMARK".getBytes(StandardCharsets.US_ASCII);

        for (int i = 0; i < payload.length; i++) {
            payload[i] = text[i % text.length];
        }

        return payload;
    }

    private static void printResult(String protocol, int run, Result result) {
        double millis = result.nanos / 1_000_000.0;

        System.out.printf(
            "%s run %d: time=%.3f ms, responses=%d/%d%n",
            protocol, run, millis, result.received, MESSAGES
        );
    }

    private static class Result {
        final long nanos;
        final int received;

        Result(long nanos, int received) {
            this.nanos = nanos;
            this.received = received;
        }
    }
}
