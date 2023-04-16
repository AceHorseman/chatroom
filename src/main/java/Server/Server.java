package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashSet;

public class Server {
    public static final int PORT = 2333;
    protected static HashSet<String> names = new HashSet<>();
    protected static HashSet<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) throws IOException {
        System.out.println("server is running...");
        ServerSocket ss = new ServerSocket(PORT);
        try {
            while (true) {
                new Procezz(ss.accept()).start();
            }
        } finally {
            ss.close();
        }
    }
}