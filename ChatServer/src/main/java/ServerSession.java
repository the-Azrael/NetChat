import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServerSession implements Session {
    private final Socket clientSocket;
    private final int sessionID;
    private User user;
    private final ServerInManager serverInManager;
    private final ServerOutManager serverOutManager;
    private volatile boolean isActive = true;

    public ServerSession(Socket clientSocket, int sessionID) throws IOException {
        this.clientSocket = clientSocket;
        this.serverInManager = new ServerInManager(
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        this.serverOutManager = new ServerOutManager(
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
        return this.serverInManager;
    }

    @Override
    public ClientServerMessagesQueueManager getOutMonitor() {
        return serverOutManager;
    }

    @Override
    public void process(Message inMessage) {
        switch (inMessage.getCommand()) {
            case Global.EXIT -> execExit();
            case Global.GET_WELCOME -> execWelcome((ClientServerMessage) inMessage);
            case Global.ECHO -> execEcho((ClientServerMessage) inMessage);
            case Global.AUTH -> execAuth((ClientServerMessage) inMessage);
            case Global.GET_USER -> execGetUser((ClientServerMessage) inMessage);
            case Global.GET_ACTIVE_USERS -> execGetActiveUsers((ClientServerMessage) inMessage);
            case Global.GET_SESSION_ID -> execGetSessionID((ClientServerMessage) inMessage);
            case Global.SEND_ALL -> execSendAll((ClientServerMessage) inMessage);
            case Global.SEND_USER -> execSendUser((ClientServerMessage) inMessage);
            case Global.CHANGE_NAME -> execChangeName((ClientServerMessage) inMessage);
        }
    }

    @Override
    public void run() {
        Thread serverInThread = new Thread(this.serverInManager);
        Thread serverOutThread = new Thread(this.serverOutManager);
        serverInThread.start();
        serverOutThread.start();
        ServerMain.writeLog(this.getClass() + " is started!");
        while (isActive && serverInThread.isAlive() && serverOutThread.isAlive()) {
            if (!serverInManager.isEmpty()) {
                ClientServerMessage inMessage = serverInManager.getMessage();
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

    private void execWelcome(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        serverOutManager.addMessage(outMessage);
        inMessage.setCommand(Global.GET_SESSION_ID);
        execGetSessionID(inMessage);
        inMessage.setCommand(Global.GET_USER);
        execGetUser(inMessage);
        inMessage.setCommand(Global.GET_ACTIVE_USERS);
        execGetActiveUsers(inMessage);
    }

    private void execExit() {
        serverInManager.deactivate();
        serverOutManager.deactivate();
        isActive = false;
    }

    private void execEcho(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        serverOutManager.addMessage(outMessage);
    }

    private void execGetUser(ClientServerMessage inMessage) {
        String[] args = new String[] {
                String.valueOf(getUser().getId()), getUser().getLogin(), getUser().getName(), getUser().getPass() };
        ClientServerMessage outMessage = new ClientServerMessage(inMessage, args);
        serverOutManager.addMessage(outMessage);
    }

    private void execChangeName(ClientServerMessage inMessage) {
        getUser().setName(inMessage.getArgument(Message.USER_NAME_IDX));
        execGetUser(inMessage);
    }

    private void execGetActiveUsers(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        List<User> activeUsers = UserManager.getActiveUsers();
        outMessage.setArguments(activeUsers.stream().map(User::getLogin).collect(Collectors.toList()));
        serverOutManager.addMessage(outMessage);
    }

    private void execGetSessionID(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        String sessionId = String.valueOf(getSessionID());
        List<String> newArgs = new ArrayList<>();
        newArgs.add(sessionId);
        outMessage.setArguments(newArgs);
        serverOutManager.addMessage(outMessage);
    }

    private void execAuth(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        User findUser = new User(inMessage.getArgument(Message.USER_IDX)
                , inMessage.getArgument(Message.USER_LOGIN_IDX)
                , inMessage.getArgument(Message.USER_NAME_IDX)
                , inMessage.getArgument(Message.USER_PASS_IDX));
        User newUser = UserManager.authorize(getUser(), findUser);
        user = newUser;
        List<String> args = Arrays.asList(
                String.valueOf(user.getId()),
                user.getLogin(),
                user.getName(),
                user.getPass()
        );
        outMessage.setArguments(args);
        serverOutManager.addMessage(outMessage);
    }

    private void execSendAll(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        List<String> newArgs = new ArrayList<>();
        newArgs.add(inMessage.getArguments().remove(Message.USER_FROM_IDX));
        newArgs.add("userTo");
        inMessage.getArguments().forEach(newArgs::add);
        for (ServerSessionThread st : SessionThreadsManager.getSessionThreads()) {
            newArgs.set(Message.USER_TO_IDX, st.getSession().getUser().getLogin());
            outMessage.setArguments(newArgs);
            StringBuilder sb = new StringBuilder();
            outMessage.getArguments().forEach(sb::append);
            st.getSession().getOutMonitor().addMessage(outMessage);
        }
    }

    private void execSendUser(ClientServerMessage inMessage) {
        ClientServerMessage outMessage = new ClientServerMessage(inMessage);
        String userTo = outMessage.getArgument(Message.USER_TO_IDX);
        for (ServerSessionThread st : SessionThreadsManager.getSessionThreads()) {
            if (st.getSession().getUser().getLogin().equalsIgnoreCase(userTo)) {
                st.getSession().getOutMonitor().addMessage(outMessage);
            }
        }
    }

}
