import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {
    private String fileName;

    public ConfigReader(String fileName) {
        this.fileName = fileName;
    }

    public ServerConfig load() throws IOException, ParseException {
        FileReader fr = new FileReader(fileName);
        JSONObject jsonServerConfig = (JSONObject) new JSONParser().parse(fr);
        String strPort = String.valueOf(jsonServerConfig.get("port"));
        int port = Integer.parseInt(strPort);
        ServerMain.writeLog(this.getClass().getName() + " config loaded from " + fileName);
        return new ServerConfig(port);
    }
}
