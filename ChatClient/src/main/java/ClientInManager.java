import java.io.BufferedReader;
import java.io.IOException;

public class ClientInManager implements QueueManager {
    private final BufferedReader in;
    private final Messages messages;
    private volatile boolean isActive = true;

    public ClientInManager(BufferedReader in) {
        this.in = in;
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
        while (isActive) {
            String inText = null;
            try {
                inText = in.readLine();
            } catch (IOException | RuntimeException e) {
                System.out.println(e.getMessage());
            }
            if (inText != null) {
                ChatMessage chatMessage = new ChatMessage(inText.split(" "));
                messages.addElement(chatMessage);
            }
        }
        try {
            in.close();
            System.out.println(this.getClass() + " is stopped!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
