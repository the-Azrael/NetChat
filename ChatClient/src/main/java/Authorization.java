import java.util.Scanner;

public class Authorization {
    public User authorize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Логин: ");
        String login = scanner.nextLine();
        System.out.println("Пароль: ");
        String pass = scanner.nextLine();
        return new User(login, "", pass);
    }
}