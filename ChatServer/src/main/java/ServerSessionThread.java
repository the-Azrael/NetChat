import java.io.IOException;
import java.net.Socket;

public class ServerSessionThread extends Thread {
    private final Session session;

    public ServerSessionThread(Socket clientSocket, int sessionId) throws IOException {
        this.session = new ServerSession(clientSocket, sessionId);
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void run() {
        session.run();
    }
}
