public interface QueueManager extends Runnable {
    Messages getMessages();
    void addMessage(ChatMessage message);
    ChatMessage getMessage();
    boolean isActive();
    void deactivate();
    boolean isEmpty();
}
