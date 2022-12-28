import java.io.IOException;
import java.net.Socket;

public class ClientSessionThread {
    private final ClientSession session;
    private final Thread sessionThread;

    public ClientSessionThread(Socket clientSocket) {
        try {
            this.session = new ClientSession(clientSocket);
            this.sessionThread = new Thread(session);
            this.sessionThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientSession getSession() {
        return session;
    }

    public Thread getSessionThread() {
        return sessionThread;
    }
}