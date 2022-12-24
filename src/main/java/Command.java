public interface Command {
    String getName();
    void setRequest(Message message);
    void setOutManager(QueueManager queueManager);
    Message getResponse();
    void execute();
}
