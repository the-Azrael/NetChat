public class User {
    private static int cnt = 0;
    private static final String DEFAULT_USER_PREFIX = "guest";
    private static final String DEFAULT_USER_NAME = "guest";
    private static final String DEFAULT_USER_PASS = "123";
    private final int id;
    private String login;
    private String name;
    private String pass;
    private boolean isAuthorized;

    public User(String login, String name, String pass) {
        cnt++;
        this.id = cnt;
        this.login = login;
        this.name = name;
        this.pass = pass;
    }

    public User(String id, String login, String name, String pass) {
        this.id = Integer.parseInt(id);
        this.login = login;
        this.name = name;
        this.pass = pass;
        this.isAuthorized = true;
    }

    public User() {
        cnt++;
        this.id = cnt;
        this.login = DEFAULT_USER_PREFIX + cnt;
        this.name = DEFAULT_USER_NAME + cnt;
        this.pass = DEFAULT_USER_PASS;
        this.setAuthorized(true);
    }

    public int getId() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getLogin() + " " + this.getPass();
    }

    public int equals(User o) {
        return (this.getId() == o.getId()
                && this.getLogin().equalsIgnoreCase(o.getLogin())
                && this.getName().equalsIgnoreCase(o.getName())
                && this.getPass().equalsIgnoreCase(o.getPass())) ? Global.EQUALS : Global.NOT_EQUALS;
    }
}
