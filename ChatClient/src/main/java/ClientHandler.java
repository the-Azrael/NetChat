import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private final String HOST = "localhost";
    private final int PORT = 50000;
    private Socket socket;
    private ClientSessionThread clientSessionThread;
    private Scanner scanner;
    private boolean isActive = true;
    private int lvl = 1;

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
            case ("1"): {
                if (isConnected()) {
                    sendAuth(new Authorization().authorize());
                } else {
                    System.out.println("Не запущено соединение с сервером!");
                }
                break;
            }
            case ("3"): {
                if (isConnected()) {
                    lvl = 2;
                }
            }
            case ("5"): {
                if (!isConnected()) {
                    connect();
                }
                break;
            }
            case ("6"): {
                deactivate();
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        while(isActive) {
            if (lvl == 1) {
                showMenu();
                handleChoice(getChoice("Выбирите пункт: "));
            } else if(lvl == 2) {
                showAllChatMenu();
                String message = getChoice(" >> ");
                ChatMessage chatMessage = new ChatMessage(Global.SEND_ALL);
                chatMessage.extendArguments(new String[] { message });
                clientSessionThread.getSession().getOutMonitor().addMessage(chatMessage);
            }
        }
    }

    private void deactivate() {
        isActive = false;
    }

    private void connect() {
        try {
            socket = new Socket(HOST, PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientSessionThread = new ClientSessionThread(socket);
        clientSessionThread.getSession().getOutMonitor().addMessage(new ChatMessage(Global.GET_SESSION_ID));
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
        System.out.println();
        clientSessionThread.getSession().getOutMonitor().addMessage(new ChatMessage(Global.GET_USER));
    }
}
