public class GetUserHandler implements Command {
    private final String name = Global.GET_USER;
    private Message request;
    private Message response;
    private QueueManager queueManager;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setRequest(Message message) {
        this.request = message;
    }

    @Override
    public void setOutManager(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Override
    public Message getResponse() {
        return response;
    }

    @Override
    public void execute() {
        response = new ChatMessage((ChatMessage) request);

    }
}
