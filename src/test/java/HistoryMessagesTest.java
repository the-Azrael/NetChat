import org.junit.Assert;
import org.junit.Test;

public class HistoryMessagesTest {
    HistoryMessages messages = new HistoryMessages();

    @Test
    public void testMessageAddAndGetAssert() {
        HistoryMessage historyMessage1 = new HistoryMessage("Hello!");
        HistoryMessage historyMessage2 = new HistoryMessage("World");
        messages.addMessage(historyMessage1);
        messages.addMessage(historyMessage2);
        HistoryMessage testMessage1 = messages.getMessage();
        HistoryMessage testMessage2 = messages.getMessage();
        Assert.assertEquals(testMessage1.getMessage(), historyMessage1.getMessage());
        Assert.assertEquals(testMessage2.getMessage(), historyMessage2.getMessage());
    }
}
