import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class HistoryMessageTest {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");
    private static String messageText1 = "Hello World!";
    private static LocalDateTime date1 = LocalDateTime.now();
    private static String messageText2 = "Bue! Bue!";
    private static LocalDateTime date2 = LocalDateTime.now();

    @Test
    public void TestMessageToString() {
        HistoryMessage historyMessage1 = new HistoryMessage(messageText1, date1);
        HistoryMessage historyMessage2 = new HistoryMessage(messageText2, date2);
        Assert.assertEquals(historyMessage1.toString(), dtf.format(date1) + "\t [Hello World!]");
        Assert.assertEquals(historyMessage2.toString(), dtf.format(date2) + "\t [Bue! Bue!]");
    }

    @Test
    public void TestFormatter() {
        LocalDate date = LocalDate.of(2000, Month.JANUARY, 1);
        LocalTime time = LocalTime.of(0, 0, 0, 0);
        LocalDateTime datetime = LocalDateTime.of(date, time);
        Assert.assertEquals(dtf.format(datetime), "01.01.2000 00:00:00.0000");
    }

}
