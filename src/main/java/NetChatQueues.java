import java.util.concurrent.ArrayBlockingQueue;

public interface NetChatQueues<T> {
    int getSize();
    ArrayBlockingQueue<T> getMessages();
    void addMessage(T element);
    T getMessage();
}
