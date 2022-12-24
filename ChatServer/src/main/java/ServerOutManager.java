import java.io.PrintWriter;

public class ServerOutManager implements QueueManager{
    private final PrintWriter out;
    private final Messages messages;
    private volatile boolean isActive = true;

    public ServerOutManager(PrintWriter out) {
        this.out = out;
        messages = new Messages(64);
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
                System.out.println("out: " + message);
                out.println(message);
                out.flush();
            }
        }
        System.out.println(this.getClass() + " is stopped!");
        out.close();
    }
}
