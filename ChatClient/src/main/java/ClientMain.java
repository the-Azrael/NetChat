import java.io.IOException;

final class ClientMain {
    public static final String moduleName = "ChatClient";
    public static int pos = -1;
    private static final String LOG_FILE_PATH = "./clientFile.log";

    public static void main(String[] args) throws IOException {
        HistoryManager.addModuleName(moduleName);
        pos = HistoryManager.getIdx(moduleName);
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.start();
        Thread logWriter = new Thread(
                new ClientServerHistoryWriter(LOG_FILE_PATH, HistoryManager.getHistoryById(pos).getHistoryMessages()));
        logWriter.start();
        writeLog("LogWriter is started!");
    }

    public static void writeLog(String message) {
        HistoryManager.addMessage(pos, message);
    }
}