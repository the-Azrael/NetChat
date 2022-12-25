import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class HistoryMessages {
    private volatile CopyOnWriteArrayList<HistoryMessage> history;

    public HistoryMessages() {
        this.history = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<HistoryMessage> getHistory() {
        return history;
    }

    public void addMessage(String msg, String className) {
        history.add(new HistoryMessage(msg, className));
    }

    public List<HistoryMessage> find(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return history.stream().filter(h -> h.getDate().isAfter(dateFrom) && h.getDate().isBefore(dateTo)).collect(Collectors.toList());
    }

    public List<HistoryMessage> find(String className) {
        return history.stream().filter(h -> h.getClassName().equals(className)).collect(Collectors.toList());
    }

}
