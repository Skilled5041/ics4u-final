package aaron.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Server {
    ServerSocket serverSocket = null;
    LinkedList<ClientHandler> clientHandlers = new LinkedList<>();
    Consumer<String> handler = null;

    public void setHandler(Consumer<String> handler) {
        this.handler = handler;
    }

    public void host(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error starting aaron.server: " + e.getMessage());
        }

        String ip = serverSocket.getInetAddress().getHostAddress();
        System.out.printf("Server started on %s:%d\n", ip, port);

        new Thread(() -> {
            while (true) {
                accept();
            }
        }).start();
    }

    public synchronized void close() {
        if (serverSocket == null) {
            System.out.println("Server is not running");
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error closing aaron.server: " + e.getMessage());
        }

        System.out.println("Server closed");
    }

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

    public void broadcast(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.send(message);
        }
    }

    public void handle(String message) {
        if (handler != null) {
            handler.accept(message);
        }
    }
}
