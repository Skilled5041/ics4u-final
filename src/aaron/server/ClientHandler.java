// Aaron Ye
// 2024-01-21
// Was for multiplayer

package aaron.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final Server server;

    /**
     * Creates a new client handler
     * @param socket Socket to connect to
     * @param server Server to handle messages
     */
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error creating aaron.client handler: " + e.getMessage());
        }
    }

    /**
     * Runs the client handler
     */
    @Override
    public void run() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = input.readLine();

                    if (message == null) {
                        System.out.println("Client disconnected");
                        close();
                        break;
                    }

                    System.out.println("Received message: " + message);
                    server.handle(message);
                } catch (IOException e) {
                    System.out.println("Error reading from aaron.client: " + e.getMessage());
                    close();
                    break;
                }
            }
        }).start();
    }

    /**
     * Sends a message to the client
     * @param message
     */
    public void send(String message) {
        output.println(message);
    }

    /**
     * Closes the connection
     */
    public void close() {
        try {
            socket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            System.out.println("Error closing aaron.client handler: " + e.getMessage());
        }
    }
}