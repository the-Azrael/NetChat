import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

interface HistoryWriter extends Runnable {
    HistoryMessages getHistoryMessages();
    String getFilePath();
    File getFile(String filePath);
    FileWriter getFileWriter();
    void setActive(boolean isActive);
    boolean isActive();

    default void write() throws IOException {
        while (!getHistoryMessages().isEmpty()) {
            try {
                HistoryMessage hm = getHistoryMessages().getMessage();
                getFileWriter().write(hm.toString());
                getFileWriter().write("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        getFileWriter().flush();
    }
}
