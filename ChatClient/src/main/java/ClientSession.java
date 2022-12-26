import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientSession implements Session {
    private Socket clientSocket;
    private int sessionID;
    private User user;
    private final ClientInManagerClientServerMessages clientInMonitor;
    private final ClientOutManagerClientServerMessages clientOutMonitor;
    private volatile boolean isActive = true;

    public ClientSession(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientInMonitor = new ClientInManagerClientServerMessages(
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        this.clientOutMonitor = new ClientOutManagerClientServerMessages(
                new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        this.sessionID = 0;
        this.user = null;
    }

    @Override
    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public int getSessionID() {
        return sessionID;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public ClientServerMessagesQueueManager getInMonitor() {
        return clientInMonitor;
    }

    @Override
    public ClientServerMessagesQueueManager getOutMonitor() {
        return clientOutMonitor;
    }

    @Override
    public void process(Message inMessage) {
        switch (inMessage.getCommand()) {
            case (Global.EXIT) -> {
                execExit();
                break;
            }
            case (Global.GET_USER) -> {
                user = new User(inMessage.getArgument(0), inMessage.getArgument(1), inMessage.getArgument(2));
                break;
            }
            case (Global.GET_SESSION_ID) -> {
                sessionID = Integer.parseInt(inMessage.getArguments()[0]);
                break;
            }
            case (Global.SEND_ALL) -> {
                String userName = inMessage.getArguments()[0];
                String message = String.join(" ",
                        Arrays.copyOfRange(inMessage.getArguments(), 1, inMessage.getArguments().length));
                System.out.println(userName + " << " + message);
            }
        }
    }

    @Override
    public void run() {
        Thread clientInThread = new Thread(this.clientInMonitor);
        Thread clientOutThread = new Thread(this.clientOutMonitor);
        clientOutThread.start();
        clientInThread.start();
        System.out.println(this.getClass() + " is started!");
        while (isActive || clientInThread.isAlive() || clientOutThread.isAlive()) {
            if (!clientInMonitor.isEmpty()) {
                ClientServerMessage inMessage = clientInMonitor.getMessage();
                process(inMessage);
            }
        }
        try {
            System.out.println(this.getClass() + " is stopped!");
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execExit() {
        clientInMonitor.deactivate();
        clientOutMonitor.deactivate();
        isActive = false;
    }
}
