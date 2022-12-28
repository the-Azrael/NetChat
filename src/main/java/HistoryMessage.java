import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryMessage {
    private final DateTimeFormatter dtf = Global.DATE_TIME_FORMATTER;
    private final LocalDateTime date;
    private final String message;

    public HistoryMessage(String message) {
        this.date = LocalDateTime.now();
        this.message = message;
    }

    public HistoryMessage(String message, LocalDateTime dt) {
        this.message = message;
        this.date = dt;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return dtf.format(getDate()) + "\t [" + getMessage() + "]";
    }
}
