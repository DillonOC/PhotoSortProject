package files;

import java.io.File;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FileMoverTest extends TestCase {
    
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
    
    /* Helpers */
    private File getTestResourceFile(String resourceName) throws Exception {
        URL resource = getClass().getClassLoader().getResource(resourceName);

        assertNotNull("Could not find test resource: " + resourceName, resource);

        return new File(resource.toURI());
    }

    public void testFileMoverCreatesDirectoriesAndMovesFile() throws Exception
    {
        // Instantiate FileMover object
        FileMover fileMover = new FileMover();

        // Set up resources
        File photo = getTestResourceFile("IMG_1697.JPG");
        Path source = photo.toPath();
        Path destination = photo.toPath().getParent().resolve("2024").resolve("01").resolve("IMG_1697.JPG");

        System.out.println(destination.toString());
        // Call the moveToDestination method
        fileMover.moveToDestination(source, destination);

        assertTrue(Files.isDirectory(destination.getParent().getParent()));
        assertTrue(Files.isDirectory(destination.getParent()));
        assertTrue(Files.isRegularFile(destination));
    }
}
