public class User {
    private static int cnt = 0;
    private static int id = 0;
    private static final String DEFAULT_USER_PREFIX = "guest";
    private static final String DEFAULT_USER_PASS = "123";
    private String login;
    private String pass;
    private boolean isAuthorized;

    public User(String login, String pass) {
        cnt++;
        this.id = cnt;
        this.login = login;
        this.pass = pass;
    }

    public User() {
        cnt++;
        this.id = cnt;
        this.login = DEFAULT_USER_PREFIX + cnt;
        this.pass = DEFAULT_USER_PASS;
        this.setAuthorized(true);
        cnt++;
    }

    public static int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setAuthorized(boolean authorized) {
        this.isAuthorized = authorized;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    @Override
    public String toString() {
        return this.getLogin() + " " + this.getPass();
    }
}
