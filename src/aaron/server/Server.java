// Aaron Ye
// 2024-01-21

package aaron.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Server {
    ServerSocket serverSocket = null;
    LinkedList<ClientHandler> clientHandlers = new LinkedList<>();
    Consumer<String> handler = null;

    /**
     * Set the handler for when a message is received
     * @param handler The handler
     */
    public void setHandler(Consumer<String> handler) {
        this.handler = handler;
    }

    /**
     * Start the server on the specified port
     * @param port The port to start the server on
     */
    public void host(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error starting server: " + e.getMessage());
        }

        String ip = serverSocket.getInetAddress().getHostAddress();
        System.out.printf("Server started on %s:%d\n", ip, port);

        new Thread(() -> {
            while (true) {
                accept();
            }
        }).start();
    }

    /**
     * Close the server
     */
    public synchronized void close() {
        if (serverSocket == null) {
            System.out.println("Server is not running");
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing server: " + e.getMessage());
        }

        System.out.println("Server closed");
    }

    /**
     * Accepts a new client connection
     */
    public synchronized void accept() {
        if (serverSocket == null) {
            System.out.println("Server is not running");
            return;
        }
        try {
            ClientHandler clientHandler = new ClientHandler(serverSocket.accept(), this);
            clientHandlers.add(clientHandler);
            clientHandler.start();
            System.out.println("Client connected");
        } catch (IOException e) {
            System.out.println("Error accepting aaron.client: " + e.getMessage());
        }
    }

    /**
     * Sends a message to all connected clients
     * @param message
     */
    public void broadcast(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(message);
        }
    }

    /**
     * Handles a message received from a client
     * @param message
     */
    public void handle(String message) {
        if (handler != null) {
            handler.accept(message);
        }
    }
}
