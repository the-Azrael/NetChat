import java.io.IOException;

final class ServerMain {
    public static String moduleName = "ChatServer";
    public static int pos = -1;
    private static final String LOG_FILE_PATH = "./serverFile.log";
    public static void main(String[] args) throws IOException {
        HistoryManager.addModuleName(moduleName);
        pos = HistoryManager.getIdx(moduleName);
        writeLog("Server application is started!");
        Server.getInstance().start();
        Thread logWriter = new Thread(
                new ClientServerHistoryWriter(LOG_FILE_PATH, HistoryManager.getHistoryById(pos).getHistoryMessages()));
        logWriter.start();
        writeLog("LogWriter is started!");
    }

    public static void writeLog(String message) {
        HistoryManager.addMessage(pos, message);
    }
}
