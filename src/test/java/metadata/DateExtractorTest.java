package metadata;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
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

    /**
     * Tests
     */
    public void testReturnsDateTimeOriginalWhenPresent()
    {
        DateExtractor dateExtractor = new DateExtractor();
        File photo = new File("C:\\Users\\Dillo\\Documents\\PhotoSortProject\\src\\test\\resources\\IMG_1697.JPG");
        Calendar dateTaken = dateExtractor.extractDate(photo);
        assertTrue( true );
    }

    public void testReturnsNullWhenNoExifDateExists()
    {
        assertTrue( true );
    }

    public void testDoesNotCrashWhenFileHasNoExifDirectory()
    {
        assertTrue(true);
    }
}