import java.util.concurrent.CopyOnWriteArrayList;

public class SessionThreadsManager extends Thread {
    private static final CopyOnWriteArrayList<ServerSessionThread> sessionThreads = new CopyOnWriteArrayList<>();
    private static int sessionCount = 0;

    public SessionThreadsManager() {
        setName("SessionThread - " + sessionCount);
    }

    public static CopyOnWriteArrayList<ServerSessionThread> getSessionThreads() {
        return sessionThreads;
    }

    @Override
    public void run() {
        ServerMain.writeLog(getName() + " is started!");
        while (true) {
            sessionThreads.removeIf(s -> !s.getSession().isActive());
        }
    }

    public int nextSessionID() {
        sessionCount++;
        return sessionCount;
    }

    public void addSessionThread(ServerSessionThread st) {
        sessionThreads.add(st);
    }
}
