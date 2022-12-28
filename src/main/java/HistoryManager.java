import java.util.concurrent.CopyOnWriteArrayList;

public class HistoryManager {
    private static CopyOnWriteArrayList<HistoryMessagesForModules> histories = new CopyOnWriteArrayList<>();

    public static int getIdx(String moduleName) {
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).moduleName.equalsIgnoreCase(moduleName)) return i;
        }
        return -1;
    }

    public static void addModuleName(String moduleName) {
        histories.add(new HistoryMessagesForModules(moduleName));
    }

    public static void addMessage(int idx, String message) {
        histories.get(idx).add(message);
    }

    public static HistoryMessagesForModules getHistoryById(int idx) {
        return histories.get(idx);
    }

    public static HistoryMessagesForModules getModuleName(String name) {
        return histories.stream().filter(a -> a.getModuleName().equals(name)).findFirst().get();
    }

}
