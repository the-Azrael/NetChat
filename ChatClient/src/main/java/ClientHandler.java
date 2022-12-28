import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private static final int mainMenuLevel = 1;
    private static final int allChatMenuLevel = 2;
    private static final int privateChatMenuLevel = 3;
    private static final String exitMainMenu = "/menu";
    private int menuLevel = mainMenuLevel;
    private ClientSessionThread clientSessionThread;
    private Scanner scanner;
    private volatile boolean isActive = true;
    private volatile String toUser = "";


    public void showUserInfo() {
        String userName = "Не авторизирован";
        int sessionId = 0;
        if (isAuthorized()) {
            userName = clientSessionThread.getSession().getUser().getLogin();
        }
        if (isConnected()) {
            sessionId = clientSessionThread.getSession().getSessionID();
        }
        System.out.println("№ сессии: " + sessionId);
        System.out.println("Пользователь: " + userName);
    }

    public void showMenu() {
        showUserInfo();
        System.out.println("[1] - Авторизация");
        System.out.println("[2] - Сменить никнейм");
        System.out.println("[3] - Общение, общий чат");
        System.out.println("[4] - Общение, приватный чат");
        System.out.println("[5] - Выход");
    }

    public void showAllChatMenu() {
        showUserInfo();
        String message = getChoice(" >> ");
        if (message.equalsIgnoreCase(exitMainMenu)) {
            menuLevel = mainMenuLevel;
        } else {
            ClientServerMessage chatMessage = new ClientServerMessage(Global.SEND_ALL);
            List<String> args = new ArrayList<>();
            args.add(clientSessionThread.getSession().getUser().getLogin());
            chatMessage.setArguments(args);
            chatMessage.addToArguments(message.split(Global.SPLITTER));
            clientSessionThread.getSession().getOutMonitor().addMessage(chatMessage);
        }
    }

    public void showPrivateChatMenu() {
        String message = getChoice(" >> ");
        if (message.equalsIgnoreCase(exitMainMenu)) {
            menuLevel = mainMenuLevel;
        } else {
            ClientServerMessage chatMessage = new ClientServerMessage(Global.SEND_USER);
            List<String> args = new ArrayList<>();
            args.add(clientSessionThread.getSession().getUser().getLogin());
            args.add(clientSessionThread.getSession().getUser().getLogin());
            args.add(message);
            chatMessage.setArguments(args);
            clientSessionThread.getSession().getOutMonitor().addMessage(chatMessage);
        }
    }

    private String getChoice(String requestText) {
        System.out.println(requestText);
        return scanner.nextLine();
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case "1" -> {
                if (isConnected()) {
                    sendAuth(new Authorization().authorize());
                } else {
                    System.out.println("Не запущено соединение с сервером!");
                }
            }
            case "2" -> {
                changeName();
            }
            case "3" -> {
                if (isConnected()) {
                    menuLevel = allChatMenuLevel;
                }
            }
            case "4" -> {
                if (isConnected()) {
                    menuLevel = privateChatMenuLevel;
                }
            }
            case "5" -> exit();
            default -> System.out.println("Введен неверный пункт меню.");
        }
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        connect();
        while(isActive) {
            if (menuLevel == 1) {
                showMenu();
                handleChoice(getChoice("Выбирите пункт: "));
            } else if (menuLevel == allChatMenuLevel) {
                showAllChatMenu();
            } else if (menuLevel == 3) {
                showPrivateChatMenu();
            }
        }
    }

    private void exit() {
        ClientServerMessage outMessage = new ClientServerMessage(Global.EXIT);
        clientSessionThread.getSession().getOutMonitor().addMessage(outMessage);
        isActive = false;
    }

    private void connect() {
        Socket socket;
        try {
            String HOST = "localhost";
            int PORT = 50000;
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientSessionThread = new ClientSessionThread(socket);
        clientSessionThread.getSession().getOutMonitor().addMessage(new ClientServerMessage(Global.GET_WELCOME));
    }

    private boolean isConnected() {
        return (clientSessionThread != null
                && clientSessionThread.getSessionThread().isAlive()
                && clientSessionThread.getSession() != null);
    }

    private boolean isAuthorized() {
        return (isConnected() && clientSessionThread.getSession().getUser() != null);
    }

    private void sendAuth(User user) {
        ClientServerMessage outMessage = new ClientServerMessage(Global.AUTH);
        outMessage.setArguments(List.of(
                new String[]{String.valueOf(user.getId()), user.getLogin(), user.getName(), user.getPass()}));
        clientSessionThread.getSession().getOutMonitor().addMessage(outMessage);
    }

    private void changeName() {
        String name = getChoice("Введите новый ник: ");
        User user = clientSessionThread.getSession().getUser();
        user.setName(name);
        ClientServerMessage outMessage = new ClientServerMessage(Global.CHANGE_NAME);
        outMessage.setArguments(List.of(
                new String[]{String.valueOf(user.getId()), user.getLogin(), user.getName(), user.getPass()}));
        clientSessionThread.getSession().getOutMonitor().addMessage(outMessage);
    }

    private void sendGetUser() {
        clientSessionThread.getSession().getOutMonitor().addMessage(new ClientServerMessage(Global.GET_USER));
    }
}