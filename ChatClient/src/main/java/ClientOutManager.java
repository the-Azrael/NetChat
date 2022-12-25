import java.io.PrintWriter;

public class ClientOutManager implements QueueManager {
    private final PrintWriter out;
    private final Messages messages;
    private volatile boolean isActive = true;

    public ClientOutManager(PrintWriter out) {
        this.out = out;
        this.messages = new Messages(16);
    }

    @Override
    public Messages getMessages() {
        return messages;
    }

    @Override
    public void addMessage(ChatMessage message) {
        messages.addElement(message);
    }

    @Override
    public ChatMessage getMessage() {
        return messages.getElement();
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void deactivate() {
        isActive = false;
    }

    @Override
    public boolean isEmpty() {
        return messages.getMessages().isEmpty();
    }

    @Override
    public void run() {
        System.out.println(this.getClass() + " is started!");
        while(isActive) {
            if (!isEmpty()) {
                ChatMessage message = messages.getElement();
                out.println(message);
                out.flush();
            }
        }
        System.out.println(this.getClass() + " is stopped!");
        out.close();
    }
}
