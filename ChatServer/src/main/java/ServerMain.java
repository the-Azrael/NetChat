
final class ServerMain {
    public static final String moduleName = "ChatServer";
    public static int pos;
    public static void main(String[] args) {
        HistoryManager.addModuleName(moduleName);
        pos = HistoryManager.getIdx(moduleName);
        writeLog(ServerMain.class.getName() + " is started!");
        Server.getInstance().start();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        HistoryMessagesForClasses h = HistoryManager.getHistoryById(pos);
                        for (HistoryMessage m : h.getHistoryMessages().getMessages()) {
                            System.out.println(m);
                        }
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }

    public static void writeLog(String message) {
        HistoryManager.addMessage(pos, message);
    }
}
