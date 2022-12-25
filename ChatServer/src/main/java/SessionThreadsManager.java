import java.util.concurrent.CopyOnWriteArrayList;

public class SessionThreadsManager extends Thread {
    private static final CopyOnWriteArrayList<SessionThread> sessionThreads = new CopyOnWriteArrayList<>();
    private static int sessionCount = 0;

    public SessionThreadsManager() {
    }

    public static CopyOnWriteArrayList<SessionThread> getSessionThreads() {
        return sessionThreads;
    }

    @Override
    public void run() {
        System.out.println(SessionThreadsManager.class + " is started!");
        while (true) {
            sessionThreads.removeIf(s -> !s.getSession().isActive());
        }
    }

    public int nextSessionID() {
        sessionCount++;
        return sessionCount;
    }

    public void addSessionThread(SessionThread st) {
        sessionThreads.add(st);
    }
}
