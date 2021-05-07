package edu.cornell.mannlib.sample;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Hello world!
 *
 */
public class App {
	
	
    public App() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        CompositeConfiguration config = AppConfig.getConfig();
        
        System.out.println(config.getString("buildDir"));
    }
	
	
}
