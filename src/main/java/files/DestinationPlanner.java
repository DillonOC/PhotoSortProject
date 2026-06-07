package files;

import java.nio.file.Path;

public class DestinationPlanner 
{

    public Path planSortedPhotoDestination(Path outputFolder)
    {
        Path destination = outputFolder.resolve("test");
        return destination;
    }

    public Path planExactDuplicatePhotoDestination(Path outputFolder)
    {
        Path destination = outputFolder.resolve("test");
        return destination;
    }
    
}
