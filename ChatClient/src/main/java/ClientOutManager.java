import java.io.PrintWriter;

public class ClientOutManager implements ClientServerMessagesQueueManager {
    private final PrintWriter out;
    private final ClientServerMessages clientServerMessages;
    private volatile boolean isActive = true;

    public ClientOutManager(PrintWriter out) {
        this.out = out;
        this.clientServerMessages = new ClientServerMessages(16);
    }

    @Override
    public ClientServerMessages getMessages() {
        return clientServerMessages;
    }

    @Override
    public void addMessage(ClientServerMessage message) {
        clientServerMessages.addMessage(message);
    }

    @Override
    public ClientServerMessage getMessage() {
        return clientServerMessages.getMessage();
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
        return clientServerMessages.getMessages().isEmpty();
    }

    @Override
    public void run() {
        System.out.println(this.getClass() + " is started!");
        while(isActive) {
            if (!isEmpty()) {
                ClientServerMessage message = clientServerMessages.getMessage();
                message.setSendTime(System.currentTimeMillis());
                System.out.println("out: " + message);
                out.println(message);
                out.flush();
            }
        }
        System.out.println(this.getClass() + " is stopped!");
        out.close();
    }
}
