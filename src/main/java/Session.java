import java.net.Socket;

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
}
