import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientSession implements Session {
    private Socket clientSocket;
    private int sessionID;
    private User user;
    private final ClientInManager clientInMonitor;
    private final ClientOutManager clientOutMonitor;
    private volatile boolean isActive = true;

    public ClientSession(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.clientInMonitor = new ClientInManager(
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        this.clientOutMonitor = new ClientOutManager(
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
    public QueueManager getInMonitor() {
        return clientInMonitor;
    }

    @Override
    public QueueManager getOutMonitor() {
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
                user = new User(inMessage.getArgument(3), inMessage.getArgument(4), inMessage.getArgument(5));
                break;
            }
            case (Global.GET_SESSION_ID) -> {
                sessionID = Integer.parseInt(inMessage.getArgument(3));
                break;
            }
            case (Global.SEND_ALL) -> {
                String userName = inMessage.getArgument(3);
                String message = String.join(" ",
                        Arrays.copyOfRange(inMessage.getArguments(), 4, inMessage.getArguments().length));
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
                ChatMessage inMessage = clientInMonitor.getMessage();
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
