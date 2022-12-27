import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private static final String CONFIG_FILE_PATH = ".//config_server.json";
    private static volatile Server INSTANCE;
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
        int port;
        try {
            port = new ConfigReader(CONFIG_FILE_PATH).load().getPort();
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
