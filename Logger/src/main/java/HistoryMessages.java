import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class HistoryMessages {
    private volatile int size;
    private volatile BlockingQueue<HistoryMessage> history;

    public HistoryMessages(int size) {
        this.size = size;
        this.history = new ArrayBlockingQueue<>(size);
    }

    public BlockingQueue<HistoryMessage> getHistory() {
        return history;
    }

    public void addMessage(String msg, String className) {
        removeMessage();
        history.offer(new HistoryMessage(msg, className));
    }

    public void removeMessage() {
        if (history.size() >= size) {
            history.remove();
        }
    }

    public List<HistoryMessage> find(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return history.stream().filter(h -> h.getDate().isAfter(dateFrom) && h.getDate().isBefore(dateTo)).collect(Collectors.toList());
    }

    public List<HistoryMessage> find(String className) {
        return history.stream().filter(h -> h.getClassName().equals(className)).collect(Collectors.toList());
    }

}
