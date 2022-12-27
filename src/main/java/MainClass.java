public interface MainClass {
    void setModuleName(String name);
    void setModuleIdx(int idx);
    HistoryManager getHistoryManager();
    int getModuleIdx();
    String getModuleName();
}
