package metadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.adobe.internal.xmp.XMPConst;
import com.adobe.internal.xmp.XMPException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.drew.metadata.xmp.XmpDirectory;

public class DateExtractor {
    
    public Calendar extractDate(Path photo) throws DateExtractionException 
    {
        // Convert Path to file 
        File photoFile = photo.toFile();

        // Initialize the calendar object
        Calendar extractedDate = null;

        // Initialise the metadata object
        Metadata metadata;

        try {
            // Read metadata
            metadata = ImageMetadataReader.readMetadata(photoFile);
        }
        catch(ImageProcessingException err) {
            throw new DateExtractionException("Image processing failed for: " + photoFile, err);
        }
        catch(IOException err) {
            throw new DateExtractionException("IO Exception for: " + photoFile, err);
        }
        
        // Checking for EXIF DateTimeOriginal
        ExifSubIFDDirectory directoryExif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (directoryExif != null) {
            // Get the Date Taken (DateTimeOriginal) from EXIF
            Date dateTaken = directoryExif.getDateOriginal();
            
            if (dateTaken != null) {
                // Convert Date to Calendar
                extractedDate = Calendar.getInstance();
                extractedDate.setTime(dateTaken);
                return extractedDate;
            } 
        } 
        
        // Checking for XMP datetime original
        XmpDirectory directoryXmp = metadata.getFirstDirectoryOfType(XmpDirectory.class);
        if (directoryXmp != null) {
            try {
                extractedDate = directoryXmp.getXMPMeta().getPropertyCalendar(XMPConst.NS_EXIF,"DateTimeOriginal");
                if (extractedDate != null) {
                    return extractedDate;
                }
            }
            catch (XMPException e) {
                 // Invalid/unreadable XMP date - continue to other metadata sources
            }
        } 
        
        // Checking for IPTC DateCreated
        IptcDirectory directoryIptc = metadata.getFirstDirectoryOfType(IptcDirectory.class);
        if (directoryIptc != null) {
            
            Date dateTaken = directoryIptc.getDateCreated();
            if (dateTaken != null) {
                // Convert Date to Calendar
                extractedDate = Calendar.getInstance();
                extractedDate.setTime(dateTaken);
                return extractedDate;
            }
        }

        // Checking for XMP datetime original
        if (directoryXmp != null) {
            try {
                extractedDate = directoryXmp.getXMPMeta().getPropertyCalendar(XMPConst.NS_PHOTOSHOP,"DateCreated");
                if (extractedDate != null) {
                    return extractedDate;
                }
            }
            catch (XMPException e) {
                 // Invalid/unreadable XMP date - continue to other metadata sources
            }
        } 

        // Checking for EXIF GPS Date
        GpsDirectory directoryExifGps = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        if (directoryExifGps != null) {
            // Get the Date Taken (DateTimeOriginal) from EXIF
            Date dateTaken = directoryExifGps.getGpsDate();
            
            if (dateTaken != null) {
                // Convert Date to Calendar
                extractedDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                extractedDate.setTime(dateTaken);
                return extractedDate;
            } 
        }
        
        return null;
    }
}
