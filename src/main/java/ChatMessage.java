import java.util.Arrays;

public class ChatMessage implements Message {
    private static int cnt = 0;
    private int id = 0;
    private int parentId = 0;
    private String command;
    private String[] arguments;


    public ChatMessage(String text) {
        this.id = getCnt();
        this.parentId = Global.NEW_COMMAND;
        this.arguments = (this.id + " " + this.parentId + " " + text).split(" ");
        this.command = getArgument(COMMAND_IDX);
    }

    public ChatMessage(String[] args) {
        try {
            this.id = Integer.parseInt(args[ID_IDX]);
            this.parentId = Integer.parseInt(args[PARENT_ID_IDX]);
        } catch (RuntimeException e) {
            this.id = getCnt();
            this.parentId = this.id;
        }
        this.arguments = args;
        this.command = args[COMMAND_IDX];
    }

    public ChatMessage(ChatMessage message) {
        this.id = getCnt();
        this.parentId = Integer.parseInt(message.getArgument(ID_IDX));
        this.command = message.getCommand();
        String args[] = Arrays.copyOfRange(message.getArguments(), COMMAND_IDX, message.getArguments().length);
        this.arguments = (this.id + " " + this.parentId + " " + String.join(" ", args)).split(" ");
    }

    @Override
    public int getCnt() {
        cnt++;
        return cnt;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public int getParentId() {
        return this.parentId;
    }

    @Override
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public void setArguments(String[] args) {
        this.arguments = args;
    }

    @Override
    public String[] getArguments() {
        return this.arguments;
    }

    @Override
    public void setArgument(int idx, String arg) {
        this.arguments[idx] = arg;
    }

    @Override
    public String getArgument(int idx) {
        return arguments[idx];
    }

    @Override
    public String toString() {
        return String.join(" ", getArguments());
    }

    @Override
    public void extendArguments(String[] data) {
        String[] extend = Arrays.copyOf(getArguments(), getArguments().length + data.length);
        System.arraycopy(data, 0, extend, getArguments().length, data.length);
        setArguments(extend);
    }
}
