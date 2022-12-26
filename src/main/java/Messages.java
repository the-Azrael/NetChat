import java.util.concurrent.ArrayBlockingQueue;

public class Messages implements NetChatQueues<ServerMessage> {
    private final int size;
    private final ArrayBlockingQueue<ServerMessage> messages;

    public Messages(int size) {
        this.size = size;
        this.messages = new ArrayBlockingQueue<>(size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ArrayBlockingQueue<ServerMessage> getMessages() {
        return messages;
    }

    @Override
    public void setMessages(ArrayBlockingQueue<ServerMessage> messages) {
        return;
    }

    @Override
    public void addElement(ServerMessage message) {
        messages.offer(message);
    }

    @Override
    public ServerMessage getElement() {
        return messages.poll();
    }
}
