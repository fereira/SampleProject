package edu.cornell.mannlib.sample;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AppConfig {

	public AppConfig() {
		// TODO Auto-generated constructor stub
	}
	
	public static CompositeConfiguration getConfig() {
		CompositeConfiguration config = new CompositeConfiguration();
		PropertiesConfiguration props = new PropertiesConfiguration();
		try {
		    props.load(ClassLoader.getSystemResourceAsStream("application.properties"));
		    config.addConfiguration(props);
		} catch (ConfigurationException e) {
		    throw new RuntimeException(e);
		}
		return config;
	}

}
