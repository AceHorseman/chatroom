package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Procezz extends Thread {
    private String name;
    private Socket socket;
    private PrintWriter printWriter;

    public Procezz(Socket accept) {
        this.socket = accept;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                printWriter.println("YOURNAME");
                name = bufferedReader.readLine();
                if (name == null) {
                    return;
                }
                synchronized (Server.names) {
                    if (!Server.names.contains(name)) {
                        Server.names.add(name);
                        break;
                    }
                }
            }

            printWriter.println("NAMEACCEPTED");
            Server.writers.add(printWriter);

            while (true) {
                String input = bufferedReader.readLine();
                if (input == null) {
                    return;
                }
                for (PrintWriter writer : Server.writers) {
                    writer.println("MESSAGE " + name + ": " + input);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //release client source
            if (name != null) {
                Server.names.remove(name);
            }
            if (printWriter != null) {
                Server.writers.remove(printWriter);
            }
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
