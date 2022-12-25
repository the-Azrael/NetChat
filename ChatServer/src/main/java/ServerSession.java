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
    public void process(Message inMessage) {
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
            case Global.SHOW_USERS -> {
                execShowUser(inMessage);
                break;
            }
            case Global.GET_SESSION_ID -> {
                execGetSessionID(inMessage);
                break;
            }
            case Global.SEND_ALL -> {
                execSendAll(inMessage);
                break;
            }
            case Global.SEND_USER -> {
                execSendUser(inMessage);
                break;
            }
            default -> {
                return;
            }
        }
    }

    @Override
    public void run() {
        Thread serverInThread = new Thread(this.serverInMonitor);
        Thread serverOutThread = new Thread(this.serverOutMonitor);
        serverInThread.start();
        serverOutThread.start();
        System.out.println(this.getClass() + " is started!");
        serverOutMonitor.addMessage(new ChatMessage(Global.WELCOME));
        while (isActive && serverInThread.isAlive() && serverOutThread.isAlive()) {
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
        outMessage.extendArguments(
                new String[] {String.valueOf(getUser().getId()), getUser().getLogin(), getUser().getPass() });
        serverOutMonitor.addMessage((ChatMessage) outMessage);
    }

    private void execShowUser(Message inMessage) {
        Message outMessage = new ChatMessage((ChatMessage) inMessage);
        String activeUsers = UserManager.getActiveUsers().toString();
        System.out.println(activeUsers);
        outMessage.extendArguments(new String[] { activeUsers });
        serverOutMonitor.addMessage((ChatMessage) outMessage);
    }

    private void execGetSessionID(Message inMessage) {
        Message outMessage = new ChatMessage((ChatMessage) inMessage);
        String sessionId = String.valueOf(getSessionID());
        outMessage.extendArguments(new String[] { sessionId });
        serverOutMonitor.addMessage((ChatMessage) outMessage);
    }

    private void execSendAll(Message inMessage) {
        Message outMessage = new ChatMessage((ChatMessage) inMessage);
        String[] args = Arrays.copyOfRange(outMessage.getArguments(), 0, 3);
        String[] msg = Arrays.copyOfRange(outMessage.getArguments(), 3,outMessage.getArguments().length);
        String[] user = {getUser().getLogin()};
        String[] extend = Arrays.copyOf(args, args.length + msg.length + user.length);
        System.arraycopy(user, 0, extend, args.length, user.length);
        System.arraycopy(msg, 0, extend, args.length + user.length, msg.length);
        System.out.println(Arrays.toString(extend));
        outMessage.setArguments(extend);
        for (ServerSessionThread st : SessionThreadsManager.getSessionThreads()) {
            st.getSession().getOutMonitor().addMessage((ChatMessage) outMessage);
        }
    }

    private void execSendUser(Message inMessage) {
        Message outMessage = new ChatMessage((ChatMessage) inMessage);
        String userName = outMessage.getArgument(3);
        String[] args = Arrays.copyOfRange(outMessage.getArguments(), 0, 3);
        String[] msg = Arrays.copyOfRange(outMessage.getArguments(), 3,outMessage.getArguments().length);
        String[] user = {getUser().getLogin()};
        String[] extend = Arrays.copyOf(args, args.length + msg.length + user.length);
        System.arraycopy(user, 0, extend, args.length, user.length);
        System.arraycopy(msg, 0, extend, args.length + user.length, msg.length);
        System.out.println(Arrays.toString(extend));
        outMessage.setArguments(extend);
        for (ServerSessionThread st : SessionThreadsManager.getSessionThreads()) {
            if (st.getSession().getUser().getLogin().equalsIgnoreCase(userName)) {
                st.getSession().getOutMonitor().addMessage((ChatMessage) outMessage);
            }
        }
    }

}
