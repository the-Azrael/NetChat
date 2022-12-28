import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;

public class ChatMessages {
    private final String fileName;
    private final LinkedBlockingDeque<ChatMessage> messages = new LinkedBlockingDeque<>();
    private final File file;

    public ChatMessages(String fileName) throws IOException {
        this.fileName = fileName;
        file = new File(fileName);
    }

    public void addChatMessage(ChatMessage chatMessage) {
        messages.offer(chatMessage);
    }

    public ChatMessage getChatMessage() {
        return messages.poll();
    }

    public void writeChatMessages() throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        while (!messages.isEmpty()) {
            fileWriter.write(getChatMessage().toString());
        }
        fileWriter.flush();
    }

}
