// Aaron Ye
// 2023-01-21
// Was for multiplayer

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

    /**
     * Sets the handler for when a message is received from the server
     * @param handler The handler
     */
    public void setHandler(Consumer<String> handler) {
        this.handler = handler;
    }

    /**
     * Connects to the server
     * @param host The host
     * @param port The port
     */
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            return;
        }

        System.out.println("Connected to server");

        // Read from the server
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
                    System.out.println("Error reading from server: " + e.getMessage());
                    break;
                }
            }
        }).start();
    }

    /**
     * Closes the connection to the server
     */
    public void close() {
        if (socket == null) {
            System.out.println("Not connected to server");
            return;
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connection to server: " + e.getMessage());
        }

        System.out.println("Closed connection to server");
    }

    /**
     * Sends a message to the server
     * @param message The message
     */
    public void send(String message) {
        if (socket == null) {
            System.out.println("Not connected to server");
            return;
        }
        output.println(message);
    }
}
