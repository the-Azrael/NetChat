import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.start();
    }
}
