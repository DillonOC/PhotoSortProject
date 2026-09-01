package sorting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import files.PhotoSortVisitor;

public class PhotoSortService {
    
    public void sortPhotos(Path input, Path output) throws IOException
    {
            PhotoSortVisitor visitor = new PhotoSortVisitor(output);
            Files.walkFileTree(input, visitor);
    }

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println(
                "Usage: PhotoSortService <input-folder> <output-folder>"
            );
            return;
        }


        Path input = Path.of(args[0]);
        Path ouput = Path.of(args[1]);

        PhotoSortService photoSortService = new PhotoSortService();

        try {
            photoSortService.sortPhotos(input, ouput);
        }
        catch(IOException e) {
            System.out.println("Unable to sort photos.");
        }
    }
}