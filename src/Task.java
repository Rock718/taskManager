import java.util.HashMap;
import java.util.Map;

public class Task {
    public String filePath;
    public Map<String,String> replacePattern = new HashMap<>();

    public Task(String filePath, Map<String,String> replacePattern){
        this.filePath = filePath;
        this.replacePattern = replacePattern;
    }
}
