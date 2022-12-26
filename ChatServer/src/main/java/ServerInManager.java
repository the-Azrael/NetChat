import java.io.BufferedReader;
import java.io.IOException;

public class ServerInManager implements ClientServerMessagesQueueManager {
    private final BufferedReader in;
    private final ClientServerMessages clientServerMessages;
    private volatile boolean isActive = true;

    public ServerInManager(BufferedReader in) {
        this.in = in;
        this.clientServerMessages = new ClientServerMessages(64);
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
        while (isActive) {
            String inText = null;
            try {
                inText = in.readLine();
            } catch (IOException | RuntimeException e) {
                break;
            }
            if (inText != null) {
                ClientServerMessage chatMessage = new ClientServerMessage(inText.split(" "));
                ServerMain.writeLog("in: " + chatMessage);
                clientServerMessages.addMessage(chatMessage);
            }
        }
        try {
            in.close();
            ServerMain.writeLog(this.getClass() + " is stopped!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
