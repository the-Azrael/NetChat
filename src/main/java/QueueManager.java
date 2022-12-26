public interface QueueManager extends Runnable {
    Messages getMessages();
    void addMessage(ServerMessage message);
    ServerMessage getMessage();
    boolean isActive();
    void deactivate();
    boolean isEmpty();
}
