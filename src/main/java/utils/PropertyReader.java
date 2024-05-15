package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import utils.PropertyReader;

public class PropertyReader {
	private static PropertyReader envProperties;

	private Properties properties;

	private PropertyReader() {
		properties = loadProperties();
	}
	
	private PropertyReader(String fileName) {
		properties = loadProperties(fileName);
	}

	private Properties loadProperties() {

		Properties props = new Properties();
	
		try {
//			fileInput = new FileInputStream(file);			
//			props.load(fileInput);
//			fileInput.close();
			InputStream cpr = PropertyReader.class.getResourceAsStream("/config.properties");		
			props.load(cpr);
			cpr.close();
			
		} catch (FileNotFoundException e) {
			//log.error("config.properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			//log.error("read failed due to: " + e.getMessage());
		}

		return props;
	}
	
	private Properties loadProperties(String fileName) {

		Properties props = new Properties();
	
		try {
//			fileInput = new FileInputStream(file);			
//			props.load(fileInput);
//			fileInput.close();
			InputStream cpr = PropertyReader.class.getResourceAsStream("/"+fileName+".properties");		
			props.load(cpr);
			cpr.close();
			
		} catch (FileNotFoundException e) {
			//log.error("config.properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			//log.error("read failed due to: " + e.getMessage());
		}

		return props;
	}

	public static PropertyReader getInstance() {
		if (envProperties == null) {
			envProperties = new PropertyReader();
		}
		return envProperties;
	}
	
	public static PropertyReader getInstance(String fileName) {
		//if (envProperties == null) {
			envProperties = new PropertyReader(fileName);
		//}
		return envProperties;
	}

	public String getProperty(String key) {
		return properties.getProperty(key).trim();
	}
	
	public boolean hasProperty(String key) {		
		return StringUtils.isNotBlank(properties.getProperty(key));
	}
}
