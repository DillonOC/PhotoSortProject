package files;
import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Set;
import java.io.FileWriter;
import hashing.ContentHasher;
import hashing.ContentHasherException;
import detection.DuplicateDetector;
import metadata.DateExtractor;
import metadata.DateExtractionException;

public class PhotoSortVisitor extends SimpleFileVisitor<Path> {

    DuplicateDetector detector = new DuplicateDetector(); 
    private final Path outputFolder;
    private static final Set<String> PHOTO_TYPES = Set.of(
        "image/jpeg",
        "image/png", // TODO: NEED TO IMPLEMENT OTHER METADATA LOCATIONS
        "image/tiff",
        "image/bmp"
        // "image/heic" TODO: IMPLEMENT
        // "image/heif" TODO: IMPLEMENT
    );

    public PhotoSortVisitor(Path outputFolder) {
        this.outputFolder = outputFolder;
    }

    private void recordFailedVisit(Path file) {
        try(FileWriter errorWriter = new FileWriter("failed_visits.txt", true)) {
            errorWriter.write(file.toString() + System.lineSeparator());
            System.out.println("Visit failed - file path written to failed_visits.txt");
        } catch(IOException e) {
            System.out.println("Visit failed - could not write to failed_vists.txt");
        }
    }

    // Call content 
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
        if (attr.isRegularFile()) {

            String contentType;

            try {
                contentType = Files.probeContentType(file);
            } catch(IOException e) {
                recordFailedVisit(file);
                return CONTINUE;
            }

            if (contentType == null || !PHOTO_TYPES.contains(contentType)) {
                return CONTINUE;
            }

            ContentHasher hasher = new ContentHasher();
            String hash = null;
            
            try {
                hash = hasher.createContentHash(file);
            } catch(hashing.ContentHasherException e) {
                recordFailedVisit(file);
                return CONTINUE;
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
                recordFailedVisit(file);
            }
        }

        return CONTINUE;
    }
}
