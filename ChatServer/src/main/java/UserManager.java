import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class UserManager {
    private static final User user1 = new User("user1", "user1");
    private static final User user2 = new User("user2", "user2");
    private static final User user3 = new User("user3", "user3");
    private static final User user4 = new User("user4", "user4");
    private static final User user5 = new User("user5", "user5");
    private static final CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();

    static {
        user1.setAuthorized(false);
        user2.setAuthorized(false);
        user3.setAuthorized(false);
        user4.setAuthorized(false);
        user5.setAuthorized(false);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
    }

    private UserManager() {
    }

    public static User getNewGuest() {
        User user = new User();
        addUser(user);
        return user;
    }

    public static void addUser(User user) {
        User newUser = find(user);
        if (newUser == null) {
            users.add(user);
        }
    }

    public static User authorize(User oldUser, User user) {
        User newUser = find(user);
        if (newUser != null) {
            logout(oldUser);
            newUser.setAuthorized(true);
            return newUser;
        }
        return oldUser;
    }

    public static User authorize(String login, String pass) {
        User newUser = find(login, pass);
        if (newUser != null) {
            newUser.setAuthorized(true);
        } else {
            newUser = getNewGuest();
        }
        return newUser;
    }

    public static void logout(User user) {
        user.setAuthorized(false);
    }

    public static User find(String login, String pass) {
        try {
            User user = users.stream()
                    .filter(u -> u.getLogin().equalsIgnoreCase(login) && u.getPass().equals(pass))
                    .findFirst()
                    .get();
            return user;
        } catch(RuntimeException e) {
            return null;
        }
    }

    public static User find(User inUser) {
        try {
            User user = users.stream()
                    .filter(u -> u.getLogin().equalsIgnoreCase(inUser.getLogin()))
                    .findFirst().get();
            return user;
        } catch(RuntimeException e) {
            return null;
        }
    }

    public static User find(int userId) {
        try {
            User user = users.stream()
                    .filter(u -> u.getId() == userId)
                    .findFirst().get();
            return user;
        } catch(RuntimeException e) {
            return null;
        }
    }

    public static List<User> getActiveUsers() {
        List<User> activeUsers = users.stream().filter(u -> u.isAuthorized()).collect(Collectors.toList());
        return activeUsers;
    }
}
