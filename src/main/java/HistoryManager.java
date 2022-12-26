import java.util.concurrent.CopyOnWriteArrayList;

public class HistoryManager {
    private static CopyOnWriteArrayList<HistoryMessagesForClasses> histories = new CopyOnWriteArrayList<>();

    public static int getIdx(String moduleName) {
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).moduleName.equalsIgnoreCase(moduleName)) return i;
        }
        return -1;
    }

    public static void addModuleName(String moduleName) {
        histories.add(new HistoryMessagesForClasses(moduleName));
    }

    public static void addMessage(int idx, String message) {
        histories.get(idx).add(message);
    }

    public static HistoryMessagesForClasses getHistoryById(int idx) {
        return histories.get(idx);
    }

    public static HistoryMessagesForClasses getModuleName(String name) {
        return histories.stream().filter(a -> a.getModuleName().equals(name)).findFirst().get();
    }

}
