import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private ClientSessionThread clientSessionThread;
    private Scanner scanner;
    private volatile boolean isActive = true;
    private int lvl = 1;
    private volatile String toUser = "";

    public void showMenu() {
        int sessionId = 0;
        String userName = "Не авторизирован";
        if (isAuthorized()) {
            userName = clientSessionThread.getSession().getUser().getLogin();
        }
        if (isConnected()) {
            sessionId = clientSessionThread.getSession().getSessionID();
        }
        System.out.println("№ сессии: " + sessionId);
        System.out.println("Пользователь: " + userName);
        System.out.println("[1] - Авторизация");
        System.out.println("[2] - Показать активных пользователей");
        System.out.println("[3] - Общение, общий чат");
        System.out.println("[4] - Общение, приватный чат");
        System.out.println("[5] - Соединение");
        System.out.println("[6] - Выход");
    }

    public void showAllChatMenu() {
        int sessionId = 0;
        String userName = "Не авторизирован";
        if (isAuthorized()) {
            userName = clientSessionThread.getSession().getUser().getLogin();
        }
        if (isConnected()) {
            sessionId = clientSessionThread.getSession().getSessionID();
        }
        System.out.println("№ сессии: " + sessionId);
        System.out.println("Пользователь: " + userName);
    }

    private String getChoice(String requestText) {
        System.out.println(requestText);
        return scanner.nextLine();
    }

    private void handleChoice(String choice) {
        switch (choice) {
            case ("1") -> {
                if (isConnected()) {
                    sendGetUser();
                } else {
                    System.out.println("Не запущено соединение с сервером!");
                }
            }
            case ("3") -> {
                if (isConnected()) {
                    lvl = 2;
                }
            }
            case ("4") -> {
                if (isConnected()) {
                    lvl = 3;
                }
            }
            case ("5") -> {
                if (!isConnected()) {
                    connect();
                }
            }
            case ("6") -> exit();
        }
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        while(isActive) {
            if (lvl == 1) {
                showMenu();
                handleChoice(getChoice("Выбирите пункт: "));
            } else if (lvl == 2) {
                showAllChatMenu();
                String message = getChoice(" >> ");
                ClientServerMessage chatMessage = new ClientServerMessage(Global.SEND_ALL);
                List<String> args = new ArrayList<>();
                args.add(clientSessionThread.getSession().getUser().getLogin());
                chatMessage.setArguments(args);
                chatMessage.addToArguments(message.split(Global.SPLITTER));
                clientSessionThread.getSession().getOutMonitor().addMessage(chatMessage);
            } else if (lvl == 3) {
                showAllChatMenu();
                String message = getChoice(" >> ");
                ClientServerMessage chatMessage = new ClientServerMessage(Global.SEND_USER);
                List<String> args = new ArrayList<>();
                args.add(clientSessionThread.getSession().getUser().getLogin());
                args.add(clientSessionThread.getSession().getUser().getLogin());
                args.add(message);
                chatMessage.setArguments(args);
                clientSessionThread.getSession().getOutMonitor().addMessage(chatMessage);
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
        clientSessionThread.getSession().getOutMonitor().addMessage(new ClientServerMessage(Global.GET_SESSION_ID));
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
        clientSessionThread.getSession().getOutMonitor().addMessage(new ClientServerMessage(Global.GET_USER));
    }

    private void sendGetUser() {
        clientSessionThread.getSession().getOutMonitor().addMessage(new ClientServerMessage(Global.GET_USER));
    }
}
