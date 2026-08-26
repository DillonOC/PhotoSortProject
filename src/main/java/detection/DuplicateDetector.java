package detection;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DuplicateDetector {

    private Map<String,Path> duplicateMap;

    // Constructor
    public DuplicateDetector()
    {
        duplicateMap = new HashMap<String, Path>();
    }

    // Method for adding to Map
    public void addToDuplicateMap(String hash, Path photo) 
    {
        duplicateMap.put(hash, photo);
    }

    public Path detectDuplicate(String hash)
    {
        return duplicateMap.get(hash);
    }
    
}
