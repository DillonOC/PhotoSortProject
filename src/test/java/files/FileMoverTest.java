package files;

import java.io.File;

import java.net.URL;

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

    

}
