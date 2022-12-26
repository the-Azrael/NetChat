import java.net.Socket;
import java.util.concurrent.Callable;

public interface Session extends Runnable {
    void setSessionID(int sessionID);
    void setActive(boolean isActive);
    boolean isActive();
    int getSessionID();
    void setUser(User user);
    User getUser();
    void setClientSocket(Socket clientSocket);
    Socket getClientSocket();
    QueueManager getInMonitor();
    QueueManager getOutMonitor();
    void process(Message inMessage);
}
