package files;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FileMoverTest extends TestCase {

    private Path tempDirectory;
    
    /**
     * @param testName
     */
    public FileMoverTest(String testName)
    {
        super( testName );
    }

    /** 
     * @return the suite of tests being tested 
     */
    public static Test suite()
    {
        return new TestSuite(FileMoverTest.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        tempDirectory = Files.createTempDirectory("file-mover-test");
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

    public void testFileMoverCreatesDirectoriesAndMovesFile() throws Exception
    {
        // Instantiate FileMover object
        FileMover fileMover = new FileMover();

        // Set up resources
        Path source = tempDirectory.resolve("input").resolve("testfile.jpg");
        Path destination = tempDirectory.resolve("2024").resolve("01").resolve("IMG_1697.JPG");

        Files.createDirectories(source.getParent());
        Files.writeString(source, "test contents");

        // Call the moveToDestination method
        fileMover.moveToDestination(source, destination);

        assertTrue(Files.isDirectory(destination.getParent().getParent()));
        assertTrue(Files.isDirectory(destination.getParent()));
        assertTrue(Files.isRegularFile(destination));
        assertFalse(Files.exists(source));
    }

    public void testFileMoverMovesFileWhenDirectoriesExist() throws Exception
    {
        // Instantiate FileMover object
        FileMover fileMover = new FileMover();

        // Set up resources
        Path source = tempDirectory.resolve("input").resolve("testfile.jpg");
        Path destination = tempDirectory.resolve("2024").resolve("01").resolve("testfile.jpg");

        Files.createDirectories(destination.getParent());
        Files.createDirectories(source.getParent());
        Files.writeString(source, "test contents");

        // Call the moveToDestination method
        fileMover.moveToDestination(source, destination);

        assertTrue(Files.isDirectory(destination.getParent().getParent()));
        assertTrue(Files.isDirectory(destination.getParent()));
        assertTrue(Files.isRegularFile(destination));
        assertFalse(Files.exists(source));
    }

    public void testFileMoverThrowsExceptionWhenFileExists() throws Exception
    {
        // Instantiate FileMover object
        FileMover fileMover = new FileMover();

        // Set up resources
        Path source = tempDirectory.resolve("input").resolve("testfile.jpg");
        Path destination = tempDirectory.resolve("2024").resolve("01").resolve("testfile.jpg");

        Files.createDirectories(destination.getParent());
        Files.writeString(destination, "destination contents");
        Files.createDirectories(source.getParent());
        Files.writeString(source, "source contents");

        // Call the moveToDestination method
        try {
            fileMover.moveToDestination(source, destination);
            fail("Expected FileAlreadyExistsException to be thrown");
        }
        catch(FileAlreadyExistsException err) {}
        assertTrue(Files.isRegularFile(source));
        assertEquals("source contents", Files.readString(source));

        assertTrue(Files.isRegularFile(destination));
        assertEquals("destination contents", Files.readString(destination));
    }
}
