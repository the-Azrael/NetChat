import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ServerSession implements Session {
    private final Socket clientSocket;
    private final int sessionID;
    private User user;
    private final ServerInManager serverInMonitor;
    private final ServerOutManager serverOutMonitor;
    private volatile boolean isActive = true;

    public ServerSession(Socket clientSocket, int sessionID) throws IOException {
        this.clientSocket = clientSocket;
        this.serverInMonitor = new ServerInManager(
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        this.serverOutMonitor = new ServerOutManager(
                new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        this.sessionID = sessionID;
        this.user = UserManager.getNewGuest();
        this.run();
    }

    @Override
    public void setSessionID(int sessionID) {
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
    }

    @Override
    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public QueueManager getInMonitor() {
        return this.serverInMonitor;
    }

    @Override
    public QueueManager getOutMonitor() {
        return serverOutMonitor;
    }

    @Override
    public void run() {
        Thread serverInThread = new Thread(this.serverInMonitor);
        Thread serverOutThread = new Thread(this.serverOutMonitor);
        serverInThread.start();
        serverOutThread.start();
        System.out.println(this.getClass() + " is started!");
        serverOutMonitor.addMessage(new ChatMessage(Global.WELCOME));
        while (isActive || serverInThread.isAlive() || serverOutThread.isAlive()) {
            if (!serverInMonitor.isEmpty()) {
                ChatMessage inMessage = serverInMonitor.getMessage();
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

    private void process(Message inMessage) {
        switch (inMessage.getCommand()) {
            case Global.EXIT -> {
                execExit();
                break;
            }
            case Global.ECHO -> {
                execEcho(inMessage);
                break;
            }
            case Global.GET_USER -> {
                execGetUser(inMessage);
                break;
            }
            default -> {
                return;
            }
        }
    }

    private void execExit() {
        serverInMonitor.deactivate();
        serverOutMonitor.deactivate();
        isActive = false;
    }

    private void execEcho(Message inMessage) {
        Message outMessage = new ChatMessage((ChatMessage) inMessage);
        serverOutMonitor.addMessage((ChatMessage) outMessage);
    }

    private void execGetUser(Message inMessage) {
        Message outMessage = new ChatMessage((ChatMessage) inMessage);
        outMessage.extendArguments(new String[] { getUser().getLogin(), getUser().getPass() });
        serverOutMonitor.addMessage((ChatMessage) outMessage);
    }

}
