import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.host(8080);
        server.setHandler(server::broadcast);

        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
    }
}