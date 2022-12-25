import java.io.*;

public class Logger {
    private final String filePath;
    private FileWriter fw;
    private File logFile;

    public Logger(String filePath) {
        this.filePath = filePath;
        this.logFile = new File(filePath);
        if (!logFile.exists()) {
            try {
                fw = new FileWriter(logFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(HistoryMessage message) {
        try {
            fw.write(message.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
