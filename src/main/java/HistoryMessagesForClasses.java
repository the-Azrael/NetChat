public final class HistoryMessagesForClasses {
    public final String moduleName;
    public final HistoryMessages historyMessages;

    public HistoryMessagesForClasses(String moduleName) {
        this.moduleName = moduleName;
        this.historyMessages = new HistoryMessages();
    }

    public String getModuleName() {
        return moduleName;
    }

    public HistoryMessages getHistoryMessages() {
        return historyMessages;
    }

    public void add(HistoryMessage hm) {
        historyMessages.addMessage(hm);
    }

    public void add(String message) {
        historyMessages.addMessage(new HistoryMessage(message));
    }
}
