package sorting;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PhotoSortServiceTest extends TestCase
{
    private Path tempDirectory;
    
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PhotoSortServiceTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PhotoSortServiceTest.class );
    }

    @Override
    protected void setUp() throws Exception
    {
        tempDirectory = Files.createTempDirectory("visitor-test");
    }

    @Override
    protected void tearDown() throws Exception
    {
        if(tempDirectory != null && Files.exists(tempDirectory)) {
            try (Stream<Path> paths = Files.walk(tempDirectory)) {
                paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        }
                        catch (IOException exception) {
                            throw new UncheckedIOException(exception);
                        }
                    });
            } 
        }
    }
    
    public void testSortPhotos() throws Exception {

        Path inputDir = tempDirectory.resolve("input");
        Path outputDir = tempDirectory.resolve("output");

        Files.createDirectory(inputDir);

        Path testPhoto =
            Path.of("src/test/resources/visitor/valid-date/IMG_1697.JPG");

        Files.copy(
            testPhoto,
            inputDir.resolve("IMG_1697.JPG")
        );

        PhotoSortService service = new PhotoSortService();

        service.sortPhotos(inputDir, outputDir);

        Path expected =
            outputDir.resolve("2024")
                    .resolve("04")
                    .resolve("IMG_1697.JPG");

        assertTrue(Files.exists(expected));
        assertTrue(Files.notExists(inputDir.resolve("IMG_1697.JPG")));
    }
}
