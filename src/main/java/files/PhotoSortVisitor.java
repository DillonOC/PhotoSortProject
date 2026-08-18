package files;
import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.io.FileWriter;
import hashing.ContentHasher;
import hashing.ContentHasherException;
import detection.DuplicateDetector;
import metadata.DateExtractor;
import metadata.DateExtractionException;

public class PhotoSortVisitor extends SimpleFileVisitor<Path> {

    DuplicateDetector detector = new DuplicateDetector(); 
    private final Path outputFolder;

    public PhotoSortVisitor(Path outputFolder) {
        this.outputFolder = outputFolder;
    }

    // NOTE: Add constructor with Path variable for outputFolder

    // Call content 
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
        if (attr.isRegularFile()) {

            // Need to add checks to make sure file is a photo and if not continue

            ContentHasher hasher = new ContentHasher();
            String hash = null;
            
            try {
                hash = hasher.createContentHash(file);
            } catch(hashing.ContentHasherException e) {
                visitFileFailed(file, e);
            }

            DestinationPlanner planner = new DestinationPlanner();

            Path duplicate = detector.detectDuplicate(hash, file);
            Path destination;
            if(duplicate == null) {

                DateExtractor dateExtractor = new DateExtractor();
                Calendar photoDate;

                try {
                    photoDate = dateExtractor.extractDate(file.toFile());
                    destination = planner.planSortedPhotoDestination(outputFolder, photoDate, file);
                } catch(DateExtractionException e) {
                    destination = planner.planNoDatePhotoDestination(outputFolder, file);
                }
                
            }
            else {
                destination = planner.planExactDuplicatePhotoDestination(file, duplicate);
            }

            FileMover fileMover = new FileMover();

            try {
                fileMover.moveToDestination(file, destination);
            } catch(IOException e) {

                // Decide what to do if file is unable to be moved
                
            }
        }
        
        return CONTINUE;
    }

    public FileVisitResult visitFileFailed(Path file,
                                       ContentHasherException exc) {
        try(FileWriter errorWriter = new FileWriter("failed_visits.txt", true)) {
            errorWriter.write(file.toString() + System.lineSeparator());
            System.out.println("Visit failed - file path written to failed_visits.txt");
        } catch(IOException e) {
            System.out.println("Visit failed - could not write to failed_vists.txt");
        }
        return FileVisitResult.CONTINUE;
    }
}
