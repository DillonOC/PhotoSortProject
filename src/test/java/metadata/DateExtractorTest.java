package metadata;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;

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
        // Create expected date calendar object for comparison.
        Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("GMT-07:00"));
        expected.clear();
        expected.set(2024, Calendar.APRIL, 10, 13, 32, 10);
        expected.set(Calendar.MILLISECOND, 979);

        // Create DateExtractor object and call the extractDate method.
        DateExtractor dateExtractor = new DateExtractor();
        File photo = new File("C:\\Users\\Dillo\\Documents\\PhotoSortProject\\src\\test\\resources\\IMG_1697.JPG");
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

    public void testReturnsNullWhenNoExifDateTimeOriginalExists()
    {
        // Create DateExtractor object and call the extractDate method.
        DateExtractor dateExtractor = new DateExtractor();
        File photo = new File("C:\\Users\\Dillo\\Documents\\PhotoSortProject\\src\\test\\resources\\IMG_1697 - Copy.JPG");
        Calendar dateTaken = dateExtractor.extractDate(photo);

        // Assertion to check date is null
        assertTrue(dateTaken == null);
    }

    public void testDoesNotCrashWhenFileHasNoExifDirectory()
    {
        assertTrue(false);
    }
}