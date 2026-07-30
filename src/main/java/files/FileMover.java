package files;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

public class FileMover {
    
    public void moveToDestination(Path source, Path destination) throws IOException {

        Files.createDirectories(destination.getParent());
        Files.move(source, destination);
        
    }
}
