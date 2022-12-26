import java.util.concurrent.ArrayBlockingQueue;

public class ClientServerMessages implements NetChatQueues<ClientServerMessage> {
    private final int size;
    private final ArrayBlockingQueue<ClientServerMessage> messages;

    public ClientServerMessages(int size) {
        this.size = size;
        this.messages = new ArrayBlockingQueue<>(size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ArrayBlockingQueue<ClientServerMessage> getMessages() {
        return messages;
    }

    @Override
    public void addMessage(ClientServerMessage message) {
        messages.offer(message);
    }

    @Override
    public ClientServerMessage getMessage() {
        return messages.poll();
    }
}
