import java.io.PrintWriter;

public class ServerOutManager implements ClientServerMessagesQueueManager {
    private final PrintWriter out;
    private final ClientServerMessages clientServerMessages;
    private volatile boolean isActive = true;

    public ServerOutManager(PrintWriter out) {
        this.out = out;
        clientServerMessages = new ClientServerMessages(64);
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
        ServerMain.writeLog(this.getClass() + " is started!");
        while(isActive) {
            if (!isEmpty()) {
                ClientServerMessage message = clientServerMessages.getMessage();
                message.setSendTime(System.currentTimeMillis());
                ServerMain.writeLog("out: " + message);
                System.out.println("out: " + message);
                out.println(message);
                out.flush();
            }
        }
        ServerMain.writeLog(this.getClass() + " is stopped!");
        out.close();
    }
}
