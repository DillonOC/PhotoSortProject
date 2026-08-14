package files;

import java.nio.file.Path;
import java.util.Calendar;

public class DestinationPlanner 
{

    public Path planSortedPhotoDestination(Path outputFolder, Calendar photoDate, Path photo)
    {
        Integer year = photoDate.get(Calendar.YEAR);
        Integer month = photoDate.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        Path destination = outputFolder.resolve(year.toString())
            .resolve(String.format("%02d",month)).resolve(photo.getFileName());
        
        return destination;
    }

    public Path planExactDuplicatePhotoDestination(Path photo, Path firstDuplicate)
    {
        Path destination = firstDuplicate.getParent()
                .resolve("Duplicates - " + firstDuplicate.getFileName().toString()).resolve(photo.getFileName());
        
        return destination;
    }

}
