import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static Server INSTANCE;
    private static int port;
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
        try {
            String currentPath = new java.io.File(".").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            port = new ConfigReader(".//config_server.json").load().getPort();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        ServerMain.writeLog(Server.class + " is started!");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
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
