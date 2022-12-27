import java.sql.Timestamp;
import java.util.List;

interface Message {
    int ID_IDX = 0;
    int PARENT_ID_IDX = ID_IDX + 1;
    int SEND_TIME_IDX = PARENT_ID_IDX + 1;
    int COMMAND_IDX = SEND_TIME_IDX + 1;
    int ARGS_FROM_IDX = COMMAND_IDX + 1;
    int USER_FROM_IDX = 0;
    int USER_TO_IDX = USER_FROM_IDX + 1;
    int MESSAGE_IDX = USER_TO_IDX + 1;
    int SESSION_ID_IDX = 0;

    int getCnt();
    void setId(int id);
    int getId();
    void setParentId(int parentId);
    int getParentId();
    void setCommand(String command);
    String getCommand();
    void setArguments(List<String> args);
    List<String> getArguments();
    void setArgument(int idx, String arg);
    String getArgument(int idx);
    String toString();
    Long getSendTime();
    void setSendTime(Long dateTime);
}
