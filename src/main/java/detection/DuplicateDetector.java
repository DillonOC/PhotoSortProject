package detection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DuplicateDetector {

    private Map<String,File> duplicateMap;

    // Constructor
    public DuplicateDetector()
    {
        duplicateMap = new HashMap<String, File>();
    }

    // Method for adding to Map
    private void addToDuplicateMap(String hash, File photo) 
    {
        duplicateMap.put(hash, photo);
    }

    public boolean detectDuplicate(String hash, File photo)
    {
        if(duplicateMap.containsKey(hash)) {
            return true;
        }
        else {
            addToDuplicateMap(hash, photo);
            return false;
        }
    }
    
}
