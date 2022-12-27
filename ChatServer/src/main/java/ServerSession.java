import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ClientServerMessagesQueueManager getInMonitor() {
        return this.serverInMonitor;
    }

    @Override
    public ClientServerMessagesQueueManager getOutMonitor() {
        return serverOutMonitor;
    }

    @Override
    public void process(Message inMessage) {
        switch (inMessage.getCommand()) {
            case Global.EXIT -> execExit();
            case Global.ECHO -> execEcho((ClientServerMessage) inMessage);
            case Global.GET_USER -> execGetUser((ClientServerMessage) inMessage);
            case Global.SHOW_USERS -> execShowUsers((ClientServerMessage) inMessage);
            case Global.GET_SESSION_ID -> execGetSessionID((ClientServerMessage) inMessage);
            case Global.SEND_ALL -> execSendAll((ClientServerMessage) inMessage);
            case Global.SEND_USER -> execSendUser((ClientServerMessage) inMessage);
        }
    }

    @Override
    public void run() {
        Thread serverInThread = new Thread(this.serverInMonitor);
        Thread serverOutThread = new Thread(this.serverOutMonitor);
        serverInThread.start();
        serverOutThread.start();
        ServerMain.writeLog(this.getClass() + " is started!");
        serverOutMonitor.addMessage(new ClientServerMessage(Global.WELCOME));
        while (isActive && serverInThread.isAlive() && serverOutThread.isAlive()) {
            if (!serverInMonitor.isEmpty()) {
                ClientServerMessage inMessage = serverInMonitor.getMessage();
                process(inMessage);
            }
        }
        try {
            ServerMain.writeLog(this.getClass() + " is stopped!");
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void execExit() {
        serverInMonitor.deactivate();
        serverOutMonitor.deactivate();
        isActive = false;
    }

    private void execEcho(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        serverOutMonitor.addMessage(outMessage);
    }

    private void execGetUser(ClientServerMessage inMessage) {
        String[] args = new String[] { String.valueOf(getUser().getId()), getUser().getLogin(), getUser().getPass() };
        ClientServerMessage outMessage = new ClientServerMessage(inMessage, args);
        serverOutMonitor.addMessage(outMessage);
    }

    private void execShowUsers(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        List<User> activeUsers = UserManager.getActiveUsers();
        outMessage.setArguments(activeUsers.stream().map(User::getLogin).collect(Collectors.toList()));
        serverOutMonitor.addMessage(outMessage);
    }

    private void execGetSessionID(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        String sessionId = String.valueOf(getSessionID());
        List<String> newArgs = new ArrayList<>();
        newArgs.add(sessionId);
        outMessage.setArguments(newArgs);
        serverOutMonitor.addMessage(outMessage);
    }

    private void execSendAll(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        List<String> newArgs = new ArrayList<>();
        newArgs.add(getUser().getLogin());
        inMessage.getArguments().forEach(newArgs::add);
        outMessage.setArguments(newArgs);
        for (ServerSessionThread st : SessionThreadsManager.getSessionThreads()) {
            st.getSession().getOutMonitor().addMessage(outMessage);
        }
    }

    private void execSendUser(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        String userName = outMessage.getArgument(Message.USER_TO_IDX);

        for (ServerSessionThread st : SessionThreadsManager.getSessionThreads()) {
            if (st.getSession().getUser().getLogin().equalsIgnoreCase(userName)) {
                st.getSession().getOutMonitor().addMessage(outMessage);
            }
        }
    }

}
