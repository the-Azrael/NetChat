import java.util.concurrent.LinkedBlockingDeque;

public class HistoryMessages {
    private LinkedBlockingDeque<HistoryMessage> messages = new LinkedBlockingDeque<>();

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public void addMessage(HistoryMessage message) {
        messages.offer(message);
    }

    public void addMessage(String message, String className) {
        messages.offer(new HistoryMessage(message));
    }

    public HistoryMessage getMessage() {
        return messages.poll();
    }

    public LinkedBlockingDeque<HistoryMessage> getMessages() {
        return messages;
    }
}
