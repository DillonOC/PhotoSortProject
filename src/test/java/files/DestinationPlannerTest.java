package files;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Calendar;

public class DestinationPlannerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DestinationPlannerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DestinationPlannerTest.class );
    }

    /* Helpers */
    private File getTestResourceFile(String resourceName) throws Exception {
        URL resource = getClass().getClassLoader().getResource(resourceName);

        assertNotNull("Could not find test resource: " + resourceName, resource);

        return new File(resource.toURI());
    }

    public void testPlanSortedPhotoDestinationBuildsYearMonthPathWithFilenameJanuary() throws Exception 
    {
        // Instantiate DestinationPlanner Object
        DestinationPlanner destinationPlanner = new DestinationPlanner();

        // Set up resources
        Path photo = getTestResourceFile("IMG_1697.JPG").toPath();

        // Create calendar object for destinationplanner input
        Calendar photoDate = Calendar.getInstance();
        photoDate.clear();
        photoDate.set(2024, Calendar.JANUARY, 10, 21, 32, 10);
        photoDate.set(Calendar.MILLISECOND, 979);

        // Create output folder path for destinationplanner input
        Path outputFolder = Path.of("output");

        // Create expected destination path for comparison.
        Path expectedPath = outputFolder.resolve("2024")
            .resolve("01").resolve(photo.getFileName());

        // Call the planSortedPhotoDestination method.
        Path plannedDestination = destinationPlanner.planSortedPhotoDestination(outputFolder, photoDate, photo);

        assertTrue(plannedDestination.equals(expectedPath));
    }

    public void testPlanExactDuplicatePhotoDestinationBuildsYearMonthPathWithFilenameJanuary() throws Exception 
    {
        // Instantiate DestinationPlanner Object
        DestinationPlanner destinationPlanner = new DestinationPlanner();

        // Set up resources
        Path photo = getTestResourceFile("IMG_1697.JPG").toPath();
        Path firstDuplicate = Path.of("test");

        // Create calendar object for destinationplanner input
        Calendar photoDate = Calendar.getInstance();
        photoDate.clear();
        photoDate.set(2024, Calendar.JANUARY, 10, 21, 32, 10);
        photoDate.set(Calendar.MILLISECOND, 979);

        // Create output folder path for destinationplanner input
        Path outputFolder = Path.of("output");

        // Create expected destination path for comparison.
        Path expectedPath = outputFolder.resolve("2024")
            .resolve("01").resolve("Duplicates - test").resolve(photo.getFileName());

        // Call the planSortedPhotoDestination method.
        Path plannedDestination = destinationPlanner.planExactDuplicatePhotoDestination(outputFolder, photoDate, photo, firstDuplicate);

        assertTrue(plannedDestination.equals(expectedPath));
    }
    
    public void testPlanSortedPhotoDestinationBuildsYearMonthPathWithFilenameDecember() throws Exception 
    {
        // Instantiate DestinationPlanner Object
        DestinationPlanner destinationPlanner = new DestinationPlanner();

        // Set up resources
        Path photo = getTestResourceFile("IMG_1697.JPG").toPath();

        // Create calendar object for destinationplanner input
        Calendar photoDate = Calendar.getInstance();
        photoDate.clear();
        photoDate.set(2024, Calendar.DECEMBER, 10, 21, 32, 10);
        photoDate.set(Calendar.MILLISECOND, 979);

        // Create output folder path for destinationplanner input
        Path outputFolder = Path.of("output");

        // Create expected destination path for comparison.
        Path expectedPath = outputFolder.resolve("2024")
            .resolve("12").resolve(photo.getFileName());

        // Call the planSortedPhotoDestination method.
        Path plannedDestination = destinationPlanner.planSortedPhotoDestination(outputFolder, photoDate, photo);

        assertTrue(plannedDestination.equals(expectedPath));
    }

    public void testPlanExactDuplicatePhotoDestinationBuildsYearMonthPathWithFilenameDecember() throws Exception 
    {
        // Instantiate DestinationPlanner Object
        DestinationPlanner destinationPlanner = new DestinationPlanner();

        // Set up resources
        Path photo = getTestResourceFile("IMG_1697.JPG").toPath();
        Path firstDuplicate = Path.of("test");

        // Create calendar object for destinationplanner input
        Calendar photoDate = Calendar.getInstance();
        photoDate.clear();
        photoDate.set(2024, Calendar.DECEMBER, 10, 21, 32, 10);
        photoDate.set(Calendar.MILLISECOND, 979);

        // Create output folder path for destinationplanner input
        Path outputFolder = Path.of("output");

        // Create expected destination path for comparison.
        Path expectedPath = outputFolder.resolve("2024")
            .resolve("12").resolve("Duplicates - test").resolve(photo.getFileName());

        // Call the planSortedPhotoDestination method.
        Path plannedDestination = destinationPlanner.planExactDuplicatePhotoDestination(outputFolder, photoDate, photo, firstDuplicate);

        assertTrue(plannedDestination.equals(expectedPath));
    }
}
