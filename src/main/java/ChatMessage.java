import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private final DateTimeFormatter dtf = Global.DATE_TIME_FORMATTER;
    private static int cnt;
    private final int id;
    private final String from;
    private final String to;
    private final LocalDateTime dateTime;
    private final String messageText;

    public ChatMessage(String from, String to, LocalDateTime dateTime, String messageText) {
        this.id = cnt++;
        this.from = from;
        this.to = to;
        this.dateTime = dateTime;
        this.messageText = messageText;
    }

    public static int getCnt() {
        return cnt;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessageText() {
        return messageText;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(this.id).append(Global.TAB_SPLITTER)
                .append(this.from).append(Global.TAB_SPLITTER)
                .append(this.to).append(Global.TAB_SPLITTER)
                .append(dtf.format(this.dateTime)).append(Global.TAB_SPLITTER)
                .append(this.messageText);
        return sb.toString();
    }
}
