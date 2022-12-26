public interface ClientServerMessagesQueueManager extends Runnable {
    ClientServerMessages getMessages();
    void addMessage(ClientServerMessage message);
    ClientServerMessage getMessage();
    boolean isActive();
    void deactivate();
    boolean isEmpty();
}
