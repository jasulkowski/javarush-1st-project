import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    public boolean isRegularFile(String filePath) {
        Path path = Path.of(filePath);

        return Files.isRegularFile(path);
    }

    public String readFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readString(path);
    }

    public void writeFile(String filePath, String content) throws IOException {
       Path path = Path.of(filePath);
       Files.writeString(path, content);
    }


}
