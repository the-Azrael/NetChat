public class HistoryMessagesForModules {
    public final String moduleName;
    public final HistoryMessages historyMessages;

    public HistoryMessagesForModules(String moduleName) {
        this.moduleName = moduleName;
        this.historyMessages = new HistoryMessages();
    }

    public String getModuleName() {
        return moduleName;
    }

    public HistoryMessages getHistoryMessages() {
        return historyMessages;
    }

    public void add(String message) {
        historyMessages.addMessage(new HistoryMessage(message));
    }
}
