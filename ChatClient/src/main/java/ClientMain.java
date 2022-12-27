final class ClientMain {
    public static final String moduleName = "ChatClient";
    public static int pos = -1;

    public static void main(String[] args) {
        HistoryManager.addModuleName(moduleName);
        pos = HistoryManager.getIdx(moduleName);
        writeLog(ClientMain.class.getName() + " is started!");
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.start();
    }

    public static void writeLog(String message) {
        HistoryManager.addMessage(pos, message);
    }
}
