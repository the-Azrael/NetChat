import java.util.concurrent.BlockingQueue;

public class Logger {
    private static Logger INSTANCE;
    private static final int SIZE = 1024;
    private static final HistoryMessages messages = new HistoryMessages(SIZE);

    private Logger() {
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger();
                }
            }
        }
        return INSTANCE;
    }

    public static void addMessage(String msg) {
        messages.addMessage(msg, getInstance().getClass().getName());
    }

    public static void addMessage(String msg, String className) {
        messages.addMessage(msg, className);
    }

    public static BlockingQueue<HistoryMessage> getMessages() {
        return messages.getHistory();
    }
}
