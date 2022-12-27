import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServerHistoryWriter implements HistoryWriter {
    private String filePath;
    private File file;
    private FileWriter fileWriter;
    private HistoryMessages historyMessages;
    private boolean isActive = true;

    public ServerHistoryWriter(String filePath, HistoryMessages historyMessages) throws IOException {
        this.filePath = filePath;
        this.file = new File(filePath);
        this.fileWriter = new FileWriter(this.file);
        this.historyMessages = historyMessages;
    }

    @Override
    public HistoryMessages getHistoryMessages() {
        return historyMessages;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public File getFile(String filePath) {
        return file;
    }

    @Override
    public FileWriter getFileWriter() {
        return fileWriter;
    }

    @Override
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void write() throws IOException {
        HistoryWriter.super.write();
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                write();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
