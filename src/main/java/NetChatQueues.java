import java.util.concurrent.ArrayBlockingQueue;

public interface NetChatQueues<T> {
    int getSize();
    ArrayBlockingQueue<T> getMessages();
    void setMessages(ArrayBlockingQueue<T> messages);
    void addElement(T element);
    T getElement();
}
