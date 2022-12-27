import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientServerMessage implements Message {
    private static int cnt = 0;
    private int id = 0;
    private int parentId = 0;
    private String command;
    private Long sendTime;
    private List<String> arguments = new ArrayList<>();

    public ClientServerMessage(String basicCommand) {
        this.id = getCnt();
        this.parentId = Global.NEW_COMMAND;
        this.sendTime = System.currentTimeMillis();
        this.command = basicCommand;
        this.arguments.add(Message.NO_ARGS);
    }

    public ClientServerMessage(String[] args) {
        try {
            this.id = Integer.parseInt(args[ID_IDX]);
            this.parentId = Integer.parseInt(args[PARENT_ID_IDX]);
            this.sendTime = Long.valueOf(args[SEND_TIME_IDX]);
            this.command = args[COMMAND_IDX];
            String[] ext_args = Arrays.copyOfRange(args, Message.ARGS_FROM_IDX, args.length);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ext_args.length; i++) {
                sb.append(ext_args[i]);
                this.arguments.add(ext_args[i]);
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public ClientServerMessage(ClientServerMessage message) {
        this.id = getCnt();
        this.parentId = message.getId();
        this.sendTime = System.currentTimeMillis();
        this.command = message.getCommand();
        this.arguments = message.getArguments();
    }

    public ClientServerMessage(ClientServerMessage message, String[] args) {
        this.id = getCnt();
        this.parentId = message.getId();
        this.sendTime = System.currentTimeMillis();
        this.command = message.getCommand();
        this.arguments = Arrays.stream(args).toList();
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
    public void setArguments(List<String> args) {
        this.arguments = args;
    }

    @Override
    public List<String> getArguments() {
        return this.arguments;
    }

    @Override
    public void setArgument(int idx, String arg) {
        this.arguments.set(idx, arg);
    }

    @Override
    public String getArgument(int idx) {
        return this.arguments.get(idx);
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

    public void addToArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            this.arguments.add(args[i]);
        }
    }

}
