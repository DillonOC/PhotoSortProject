package metadata;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.net.URL;
import java.util.Calendar;


/**
 * Unit test for DateExtractor.
 */
public class DateExtractorTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DateExtractorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DateExtractorTest.class );
    }

    /* Helpers */
    private File getTestResourceFile(String resourceName) throws Exception {
        URL resource = getClass().getClassLoader().getResource(resourceName);

        assertNotNull("Could not find test resource: " + resourceName, resource);

        return new File(resource.toURI());
    }
    
    /**
     * Tests
     */
    public void testReturnsDateTimeOriginalWhenPresent() throws Exception
    {
        // Create expected date calendar object for comparison.
        Calendar expected = Calendar.getInstance();
        expected.clear();
        expected.set(2024, Calendar.APRIL, 10, 21, 32, 10);
        expected.set(Calendar.MILLISECOND, 979);

        // Create DateExtractor object and call the extractDate method.
        DateExtractor dateExtractor = new DateExtractor();
        File photo = getTestResourceFile("IMG_1697.JPG");
        
        Calendar dateTaken = dateExtractor.extractDate(photo);

        // Assertions to check date is as expected.
        assertTrue(dateTaken.get(Calendar.YEAR) == expected.get(Calendar.YEAR));
        assertTrue(dateTaken.get(Calendar.MONTH) == expected.get(Calendar.MONTH));
        assertTrue(dateTaken.get(Calendar.DAY_OF_MONTH) == expected.get(Calendar.DAY_OF_MONTH));
        assertTrue(dateTaken.get(Calendar.HOUR_OF_DAY) == expected.get(Calendar.HOUR_OF_DAY));
        assertTrue(dateTaken.get(Calendar.MINUTE) == expected.get(Calendar.MINUTE));
        assertTrue(dateTaken.get(Calendar.SECOND) == expected.get(Calendar.SECOND));
        assertTrue(dateTaken.get(Calendar.MILLISECOND) == expected.get(Calendar.MILLISECOND));

    }

    public void testReturnsNullWhenNoExifDateTimeOriginalExists() throws Exception
    {
        // Create DateExtractor object and call the extractDate method.
        DateExtractor dateExtractor = new DateExtractor();
        File photo = getTestResourceFile("IMG_1697-no-datetimeoriginal.JPG");
        Calendar dateTaken = dateExtractor.extractDate(photo);

        // Assertion to check date is null
        assertTrue(dateTaken == null);
    }

    public void testDoesNotCrashWhenFileHasNoExifDirectory() throws Exception
    {
        // Create DateExtractor object and call the extractDate method.
        DateExtractor dateExtractor = new DateExtractor();
        File photo = getTestResourceFile("IMG_1697-no-metadata.JPG");
        Calendar dateTaken = dateExtractor.extractDate(photo);

        // Assertion to check date is null
        assertTrue(dateTaken == null);
    }

    public void testThrowsDateExtractionExceptionWhenFileInvalid() throws Exception
    {
        // Create DateExtractor object and call the extractDate method.
        DateExtractor dateExtractor = new DateExtractor();
        File photo = getTestResourceFile("invalid_image.jpg");

        // Catch the expected DateExtractionException and fail if not
        try {
            dateExtractor.extractDate(photo);
            fail("Expected DateExtractionException to be thrown");
        }
        catch(DateExtractionException err) {}
    }

        public void testThrowsErrorWhenFileNotFound() throws Exception
        {
            // Create DateExtractor object.
            DateExtractor dateExtractor = new DateExtractor();

            // Set up resource.
            File photo = new File("this-file-should-not-exist.jpg");

            // Catch the expected DateExtractionException and fail if not
            try {
                dateExtractor.extractDate(photo);
                fail("Expected DateExtractionException to be thrown");
            }
            catch(DateExtractionException err) {}
        }
}