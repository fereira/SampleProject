package edu.cornell.mannlib.sample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith; 
import org.junit.After;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled; 

/**
 * Unit test for simple App.
 */
public class AppTest {
    

    /**
     * Rigourous Test :-)
     */
	@Test
    public void testApp()
    {
        assertTrue( true );
    }
	
	@Disabled
    public void testApp2()
    {
        assertTrue( false );
    }
}
