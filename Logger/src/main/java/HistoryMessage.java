import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryMessage {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");
    private final LocalDateTime date;
    private final String className;
    private final String message;

    public HistoryMessage(String message, String className) {
        this.date = LocalDateTime.now();
        this.message = message;
        this.className = className;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return dtf.format(getDate()) + "\t" + getMessage() + "\t" + getClassName();
    }
}
