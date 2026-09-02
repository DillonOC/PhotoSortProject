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
            } catch(ContentHasherException e) {
                recordFailedVisit(file);
                return CONTINUE;
            }

            DestinationPlanner planner = new DestinationPlanner();

            Path duplicate = detector.detectDuplicate(hash);

            boolean addToDetector = duplicate == null;

            FileMover fileMover = new FileMover();

            Path destination;

            if(duplicate == null) {

                DateExtractor dateExtractor = new DateExtractor();
                Calendar photoDate;

                try {
                    photoDate = dateExtractor.extractDate(file);
                    if(photoDate != null) { 
                        destination = planner.planSortedPhotoDestination(outputFolder, photoDate, file);
                    }
                    else {
                        destination = planner.planNoDatePhotoDestination(outputFolder, file);
                    }
                } catch(DateExtractionException e) {
                    destination = planner.planNoDatePhotoDestination(outputFolder, file);
                }
                
            }
            else {

                if("No_date".equals(duplicate.getParent().getFileName().toString())) {

                    DateExtractor dateExtractor = new DateExtractor();
                    Calendar photoDate;

                    try {
                        photoDate = dateExtractor.extractDate(file);
                        if(photoDate != null) { 

                            Path datedDestination = planner.planSortedPhotoDestination(outputFolder, photoDate, file);

                            Path oldDuplicateDestination = planner.planExactDuplicatePhotoDestination(duplicate, datedDestination);

                            try {
                                // Promote the dated copy
                                fileMover.moveToDestination(file, datedDestination);

                                // Update map with dated destination
                                detector.addToDuplicateMap(hash, datedDestination);

                                try {
                                    fileMover.moveToDestination(duplicate, oldDuplicateDestination);
                                }
                                catch (IOException e) {
                                    System.out.println(
                                        "Unable to move old no-date duplicate: " + duplicate
                                    );
                                }
                                
                            } catch(IOException e) {
                                recordFailedVisit(file);
                            }
                            
                            return CONTINUE;
                        }

                        else {

                            destination = planner.planExactDuplicatePhotoDestination(file, duplicate);
                        }

                    } catch(DateExtractionException e) {
                        destination = planner.planExactDuplicatePhotoDestination(file, duplicate);
                    }
                }
                
                else {
                    destination = planner.planExactDuplicatePhotoDestination(file, duplicate);
                }

            }

            try {
                fileMover.moveToDestination(file, destination);
                if(addToDetector) {
                    detector.addToDuplicateMap(hash, destination);
                }
            } catch(IOException e) {
                recordFailedVisit(file);
            }
        }

        return CONTINUE;
    }
}
