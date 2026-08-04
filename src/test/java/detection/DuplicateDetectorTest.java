package detection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;

public class DuplicateDetectorTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DuplicateDetectorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DuplicateDetectorTest.class );
    }

    /* Helpers */
    private File getTestResourceFile(String resourceName) throws Exception {
        URL resource = getClass().getClassLoader().getResource(resourceName);

        assertNotNull("Could not find test resource: " + resourceName, resource);

        return new File(resource.toURI());
    }

    public void testFirstHashIsNotDuplicate() throws Exception 
    {
        // Instantiate DuplicateDetector Object
        DuplicateDetector duplicateDetector = new DuplicateDetector();

        // Set up resources
        Path photo = getTestResourceFile("IMG_1697.JPG").toPath();
        String hash = "test";

        // Call duplicate detector
        assertTrue(duplicateDetector.detectDuplicate(hash, photo) == null);
    }

    public void testSameHashDetectedAsDuplicate() throws Exception 
    {
        // Note: Two duplicate but different images are used here to ensure only the hash is considered.
        // Instantiate DuplicateDetector Object
        DuplicateDetector duplicateDetector = new DuplicateDetector();

        // Set up resources
        Path photo1 = getTestResourceFile("IMG_1697.JPG").toPath();
        Path photo2 = getTestResourceFile("IMG_1697-no-metadata.JPG").toPath();
        String hash = "test";

        // Call duplicate detector
        assertTrue(duplicateDetector.detectDuplicate(hash, photo1) == null);
        assertTrue(duplicateDetector.detectDuplicate(hash, photo2).equals(photo1));
    }

    public void testDifferentHashIsNotDuplicate() throws Exception
    {
        // Instantiate DuplicateDetector Object
        DuplicateDetector duplicateDetector = new DuplicateDetector();

        // Set up resources
        Path photo1 = getTestResourceFile("IMG_1697.JPG").toPath();
        Path photo2 = getTestResourceFile("IMG_1697-no-metadata.JPG").toPath();
        String hash1 = "test1";
        String hash2 = "test2";

        // Call duplicate detector
        assertTrue(duplicateDetector.detectDuplicate(hash1, photo1) == null);
        assertTrue(duplicateDetector.detectDuplicate(hash2, photo2) == null);
    }

    public void testNewDetectorStartsEmpty() throws Exception
    {
        // Instantiate DuplicateDetector Object
        DuplicateDetector duplicateDetector1 = new DuplicateDetector();
        DuplicateDetector duplicateDetector2 = new DuplicateDetector();

        // Set up resources
        Path photo = getTestResourceFile("IMG_1697.JPG").toPath();       
        String hash = "test";

        // Call duplicate detector
        assertTrue(duplicateDetector1.detectDuplicate(hash, photo) == null);
        assertTrue(duplicateDetector1.detectDuplicate(hash, photo).equals(photo));
        assertTrue(duplicateDetector2.detectDuplicate(hash, photo) == null);
    }
}
