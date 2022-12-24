import java.util.concurrent.CopyOnWriteArrayList;

public class SessionsManager extends Thread {
    private static final CopyOnWriteArrayList<ServerSession> sessions = new CopyOnWriteArrayList<>();
    private static int sessionCount = 0;

    public SessionsManager() {
    }

    public CopyOnWriteArrayList<ServerSession> getSessions() {
        return sessions;
    }

    @Override
    public void run() {
        System.out.println(SessionsManager.class + " is started!");
        while (true) {
            sessions.removeIf(s -> !s.isActive());
        }
    }

    public int nextSessionID() {
        sessionCount++;
        return sessionCount;
    }

    public void addSession(ServerSession session) {
        sessions.add(session);
    }
}
