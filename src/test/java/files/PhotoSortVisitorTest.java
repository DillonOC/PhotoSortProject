package files;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PhotoSortVisitorTest extends TestCase {
    
    private Path tempDirectory;

    /**
     * @param testName
     */
    public PhotoSortVisitorTest(String testName)
    {
        super( testName );
    }

    /** 
     * @return the suite of tests being tested 
     */
    public static Test suite()
    {
        return new TestSuite(PhotoSortVisitorTest.class);
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

    public void testMovesUniqueDatedPhoto() throws Exception {

        Path inputDir = tempDirectory.resolve("input");
        Files.createDirectory(inputDir);

        Path testPhoto =
                Path.of("src/test/resources/visitor/valid-date/IMG_1697.JPG");

        Files.copy(
                testPhoto,
                inputDir.resolve("IMG_1697.JPG")
        );

        Path outputDir = tempDirectory.resolve("output");

        Path expectedPath = outputDir.resolve("2024")
            .resolve("04").resolve("IMG_1697.JPG");

        PhotoSortVisitor visitor = new PhotoSortVisitor(outputDir);

        Files.walkFileTree(inputDir, visitor);

        assertTrue(Files.exists(expectedPath));
        assertTrue(Files.exists(expectedPath.getParent()));
        assertTrue(Files.exists(expectedPath.getParent().getParent()));
        assertTrue(Files.exists(outputDir));
        assertTrue(Files.notExists(inputDir.resolve("IMG_1697.JPG")));
    }

    public void testMovesDuplicatePhoto() throws Exception {

        Path inputDir = tempDirectory.resolve("input");
        Files.createDirectory(inputDir);

        Path testPhoto1 =
                Path.of("src/test/resources/visitor/duplicates/IMG_1697.JPG");

        Path testPhoto2 = 
                Path.of("src/test/resources/visitor/duplicates/IMG_1697-no-datetimeoriginal.JPG");

        Files.copy(
                testPhoto1,
                inputDir.resolve("IMG_1697.JPG")
        );

        Files.copy(
                testPhoto2,
                inputDir.resolve("IMG_1697-no-datetimeoriginal.JPG")
        );

        Path outputDir = tempDirectory.resolve("output");

        Path expectedPathNonDupe = outputDir.resolve("2024")
            .resolve("04").resolve("IMG_1697.JPG");

        Path expectedPathDupe = expectedPathNonDupe.getParent()
            .resolve("Duplicates - IMG_1697.JPG").resolve("IMG_1697-no-datetimeoriginal.JPG");


        PhotoSortVisitor visitor = new PhotoSortVisitor(outputDir);

        Files.walkFileTree(inputDir, visitor);

        assertTrue(Files.exists(expectedPathNonDupe));
        assertTrue(Files.exists(expectedPathNonDupe.getParent()));
        assertTrue(Files.exists(expectedPathNonDupe.getParent().getParent()));
        assertTrue(Files.exists(outputDir));
        assertTrue(Files.notExists(inputDir.resolve("IMG_1697.JPG")));

        assertTrue(Files.exists(expectedPathDupe));
        assertTrue(Files.exists(expectedPathDupe.getParent()));
        assertTrue(Files.exists(expectedPathDupe.getParent().getParent()));
        assertTrue(Files.exists(expectedPathDupe.getParent().getParent().getParent()));
        assertTrue(Files.exists(outputDir));
        assertTrue(Files.notExists(inputDir.resolve("IMG_1697-no-datetimeoriginal.JPG")));
    }

    public void testMovesNoDatePhoto() throws Exception {

        Path inputDir = tempDirectory.resolve("input");
        Files.createDirectory(inputDir);

        Path testPhoto =
                Path.of("src/test/resources/visitor/no-date/IMG_1697-no-datetimeoriginal.JPG");

        Files.copy(
                testPhoto,
                inputDir.resolve("IMG_1697-no-datetimeoriginal.JPG")
        );

        Path outputDir = tempDirectory.resolve("output");

        Path expectedPath = outputDir.resolve("No_date").resolve("IMG_1697-no-datetimeoriginal.JPG");

        PhotoSortVisitor visitor = new PhotoSortVisitor(outputDir);

        Files.walkFileTree(inputDir, visitor);

        assertTrue(Files.exists(expectedPath));
        assertTrue(Files.exists(expectedPath.getParent()));
        assertTrue(Files.exists(outputDir));
        assertTrue(Files.notExists(inputDir.resolve("IMG_1697-no-datetimeoriginal.JPG")));
    }
}
