import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static Server INSTANCE;
    private static final int PORT = 50000;
    private static SessionThreadsManager sessionsManager;

    private Server() {
        sessionsManager = new SessionThreadsManager();
    }

    public static Server getInstance() {
        if (INSTANCE == null) {
            synchronized (Server.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Server();
                    INSTANCE.setName(Server.class.getName());
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        System.out.println(Server.class + " is started!");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            sessionsManager.start();
            while (isAlive()) {
                Socket clientSocket = serverSocket.accept();
                ServerSessionThread sessionThread = new ServerSessionThread(clientSocket, sessionsManager.nextSessionID());
                sessionsManager.addSessionThread(sessionThread);
                sessionThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
