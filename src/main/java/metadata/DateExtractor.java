package metadata;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class DateExtractor {
    
    public Calendar extractDate(File photo) throws DateExtractionException {
        // Initialize the calendar object
        Calendar extractedDate = null;

        // Initialise the metadata object
        Metadata metadata;

        try {
            // Read metadata
            metadata = ImageMetadataReader.readMetadata(photo);
        }
        catch(ImageProcessingException err) {
            throw new DateExtractionException("Image processing failed for: " + photo, err);
        }
        catch(IOException err) {
            throw new DateExtractionException("IO Exception for: " + photo, err);
        }
        
        // Checking for EXIF Metadata
        ExifSubIFDDirectory directoryExif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        
        if (directoryExif != null) {
            // Get the Date Taken (DateTimeOriginal) from EXIF
            Date dateTaken = directoryExif.getDateOriginal();
            
            if (dateTaken != null) {
                // Convert Date to Calendar
                extractedDate = Calendar.getInstance();
                extractedDate.setTime(dateTaken);
                System.out.println("Date Taken: " + extractedDate.getTime());
            } 
            else {
                System.out.println("No EXIF DateTimeOriginal tag found.");
            }
        } 
        else {
            System.out.println("No EXIF metadata found.");
        }
        
        return extractedDate;
    }
}
