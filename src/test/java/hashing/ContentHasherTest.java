package hashing;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.imaging.ImagingException;

public class ContentHasherTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ContentHasherTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ContentHasherTest.class );
    }

    /* Helpers */
    private File getTestResourceFile(String resourceName) throws Exception {
        URL resource = getClass().getClassLoader().getResource(resourceName);

        assertNotNull("Could not find test resource: " + resourceName, resource);

        return new File(resource.toURI());
    }

    public void testSameImageWithAndWithoutMetadataReturnsSameHash() throws Exception
    {
        // Create ContentHasher object
        ContentHasher contentHasher = new ContentHasher();

        // Set up resources.
        File photo1 = getTestResourceFile("IMG_1697.JPG");
        File photo2 = getTestResourceFile("IMG_1697 - Copy (2).JPG");
        
        // Call hash method.
        String hash1 = contentHasher.createContentHash(photo1);
        String hash2 = contentHasher.createContentHash(photo2);

        // Assertion
        assertEquals(hash1, hash2);
    }

    public void testDifferentImagesReturnDifferentHashes() throws Exception
    {
        // Create ContentHasher object
        ContentHasher contentHasher = new ContentHasher();

        // Set up resources.
        File photo1 = getTestResourceFile("IMG_1697.JPG");
        File photo2 = getTestResourceFile("Y99.JPG");
        
        // Call hash method.
        String hash1 = contentHasher.createContentHash(photo1);
        String hash2 = contentHasher.createContentHash(photo2);

        // Assertion
        assertFalse(hash1.equals(hash2));
    }

    public void testSameImageReturnsSameHashWhenHashedTwice() throws Exception
    {
        // Create ContentHasher object
        ContentHasher contentHasher = new ContentHasher();

        // Set up resources.
        File photo1 = getTestResourceFile("IMG_1697.JPG");
        
        // Call hash method.
        String hash1 = contentHasher.createContentHash(photo1);
        String hash2 = contentHasher.createContentHash(photo1);

        // Assertion
        assertEquals(hash1, hash2);
    }

    public void testInvalidImageThrowsHashingException() throws Exception
    {
        // Create ContentHasher object
        ContentHasher contentHasher = new ContentHasher();

        // Set up resources.
        File photo1 = getTestResourceFile("invalid_image.jpg");
        
        // Call hash method and catch ContentHasherException
        try {
            contentHasher.createContentHash(photo1);
            fail("Expected ContentHasherException to be thrown");
        }
        catch(ContentHasherException err) {}
    }
}
