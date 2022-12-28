import java.time.format.DateTimeFormatter;

public class Global {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSS");
    public static final String EXIT = "/exit";
    public static final String ECHO = "/echo";
    public static final String GET_WELCOME = "/welcome";
    public static final String GET_USER = "/get_user";
    public static final String CHANGE_NAME = "/change_name";
    public static final String AUTH = "/auth";
    public static final String GET_ACTIVE_USERS = "/show_users";
    public static final String GET_SESSION_ID = "/get_session_id";
    public static final String SEND_USER = "/send_user";
    public static final String SEND_ALL = "/send_all";
    public static final int NEW_COMMAND = 0;
    public static final String SPLITTER = " ";
    public static final String TAB_SPLITTER = "\t";
    public static final int NOT_EQUALS = -1;
    public static final int EQUALS = 0;
}
