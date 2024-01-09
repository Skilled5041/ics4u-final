package aaron.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    Consumer<String> handler;

    public void setHandler(Consumer<String> handler) {
        this.handler = handler;
    }

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.out.println("Error connecting to aaron.server: " + e.getMessage());
            return;
        }

        System.out.println("Connected to aaron.server");

        new Thread(() -> {
            while (true) {
                if (socket.isClosed()) {
                    break;
                }

                try {
                    String message = input.readLine();
                    if (handler != null) {
                        handler.accept(message);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from aaron.server: " + e.getMessage());
                    break;
                }
            }
        }).start();
    }

    public void close() {
        if (socket == null) {
            System.out.println("Not connected to aaron.server");
            return;
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connection to aaron.server: " + e.getMessage());
        }

        System.out.println("Closed connection to aaron.server");
    }

    public void send(String message) {
        if (socket == null) {
            System.out.println("Not connected to aaron.server");
            return;
        }
        output.println(message);
    }
}
