import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSession implements Session {
    private Socket clientSocket;
    private int sessionID;
    private User user;
    private final ClientInManager clientInMonitor;
    private final ClientOutManager clientOutMonitor;
    private volatile boolean isActive = true;
    private List<String> activeUsers = new ArrayList<>();

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
            case Global.GET_WELCOME -> System.out.println("Приветствие от сервера!");
            case Global.AUTH, Global.GET_USER, Global.CHANGE_NAME -> execGetUser((ClientServerMessage) inMessage);
            case Global.EXIT -> execExit();
            case Global.GET_SESSION_ID -> execSession((ClientServerMessage) inMessage);
            case Global.GET_ACTIVE_USERS -> execGetActiveUsers((ClientServerMessage) inMessage);
            case Global.SEND_ALL -> execSendAll((ClientServerMessage) inMessage);
            default -> throw new IllegalStateException("Unexpected value: " + inMessage.getCommand());
        }
    }

    @Override
    public void run() {
        Thread clientInThread = new Thread(this.clientInMonitor);
        clientInThread.start();
        Thread clientOutThread = new Thread(this.clientOutMonitor);
        clientOutThread.start();
        ClientMain.writeLog(this.getClass() + " is started!");

        while (isActive || clientInThread.isAlive() || clientOutThread.isAlive()) {
            if (!clientInMonitor.isEmpty()) {
                ClientServerMessage inMessage = clientInMonitor.getMessage();
                process(inMessage);
            }
        }
        try {
            ClientMain.writeLog(this.getClass() + " is stopped!");
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

    private void execSession(ClientServerMessage inMessage) {
        sessionID = Integer.parseInt(inMessage.getArgument(Message.SESSION_IDX));
    }

    private void execGetUser(ClientServerMessage inMessage) {
        user = new User(inMessage.getArgument(Message.USER_IDX)
                , inMessage.getArgument(Message.USER_LOGIN_IDX)
                , inMessage.getArgument(Message.USER_NAME_IDX)
                , inMessage.getArgument(Message.USER_PASS_IDX));
    }

    private void execGetActiveUsers(ClientServerMessage inMessage) {
        List<String> st = inMessage.getArguments();
        System.out.println(st.toString());
    }

    private void execSendAll(ClientServerMessage inMessage) {
        List<String> l = inMessage.getArguments();
        String userTo = l.remove(0);
        String userFrom = l.remove(0);
        StringBuilder message = new StringBuilder();
        l.forEach(message::append);
        System.out.println(userFrom + " >> " + message);
    }

    public void setActiveUsers(List<String> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public List<String> getActiveUsers() {
        return activeUsers;
    }
}