import java.util.concurrent.ArrayBlockingQueue;

public class Messages implements NetChatQueues<ChatMessage> {
    private final int size;
    private final ArrayBlockingQueue<ChatMessage> messages;

    public Messages(int size) {
        this.size = size;
        this.messages = new ArrayBlockingQueue<>(size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ArrayBlockingQueue<ChatMessage> getMessages() {
        return messages;
    }

    @Override
    public void setMessages(ArrayBlockingQueue<ChatMessage> messages) {
        return;
    }

    @Override
    public void addElement(ChatMessage message) {
        messages.offer(message);
    }

    @Override
    public ChatMessage getElement() {
        return messages.poll();
    }
}
