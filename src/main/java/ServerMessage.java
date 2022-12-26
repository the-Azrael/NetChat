import java.sql.Timestamp;
import java.util.Arrays;

public class ServerMessage implements Message {
    //private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");
    private static int cnt = 0;
    private int id = 0;
    private int parentId = 0;
    private String command;
    private Long sendTime;
    private String[] arguments;

    public ServerMessage(String basicCommand) {
        this.id = getCnt();
        this.parentId = Global.NEW_COMMAND;
        this.sendTime = System.currentTimeMillis();
        this.command = basicCommand;
        this.arguments = new String[] {"no-args"};
    }

    public ServerMessage(String[] args) {
        try {
            this.id = Integer.parseInt(args[ID_IDX]);
            this.parentId = Integer.parseInt(args[PARENT_ID_IDX]);
            this.command = args[COMMAND_IDX];
            this.sendTime = Long.valueOf(args[SEND_TIME_IDX]);
            this.arguments = Arrays.copyOfRange(args, ARGS_FROM_IDX, args.length);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public ServerMessage(ServerMessage message) {
        this.id = getCnt();
        this.parentId = message.getId();
        this.command = message.getCommand();
        this.sendTime = message.getSendTime();
        this.arguments = message.getArguments();
    }

    public ServerMessage(ServerMessage message, String[] args) {
        this.id = getCnt();
        this.parentId = message.getId();
        this.command = message.getCommand();
        this.sendTime = message.getSendTime();
        this.arguments = args;
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
        return new StringBuilder().append(this.id).append(" ")
                .append(this.parentId).append(" ")
                .append(this.sendTime).append(" ")
                .append(this.command).append(" ")
                .append(String.join(" ", this.arguments))
                .toString();
    }

    @Override
    public Long getSendTime() {
        return sendTime;
    }

    @Override
    public void setSendTime(Long dateTime) {
        sendTime = dateTime;
    }

}
