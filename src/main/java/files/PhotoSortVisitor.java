package files;
import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.FileWriter;
import hashing.ContentHasher;
import hashing.ContentHasherException;
import detection.DuplicateDetector;

public class PhotoSortVisitor extends SimpleFileVisitor<Path> {

    DuplicateDetector detector = new DuplicateDetector(); 
    private final Path outputFolder;

    // NOTE: Add constructor with Path variable for outputFolder

    // Call content 
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) {
        if (attr.isRegularFile()) {
            ContentHasher hasher = new ContentHasher();
            String hash = null;
            
            try {
                hash = hasher.createContentHash(file);
            }
            catch(hashing.ContentHasherException e) {
                visitFileFailed(file, e);
            }

            //Add date extraction

            DestinationPlanner planner = new DestinationPlanner();

            Path duplicate = detector.detectDuplicate(hash, file);
            if(duplicate == null) {
                planner.planSortedPhotoDestination(outputFolder, );
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
