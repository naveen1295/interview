package utils;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.BeforeSuite;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;


public class BaseClass{
	
	 protected Properties properties;
	 protected RequestSpecification requestSpec;
	 RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();	 
	 
	@BeforeSuite(alwaysRun=true)
	public void setUpConfig() {
		System.out.println("in setup");
		properties = new Properties();
		try {
			String propertiesPath =System.getProperty("user.dir") + "\\src\\test\\resources\\data.properties";
			FileInputStream fis = new FileInputStream(propertiesPath);
			properties.load(fis);
		}catch (Exception e) { 
			System.out.println("exception while reading properties file. " + e.getMessage());
		}

		
		requestSpecBuilder.setBaseUri(properties.getProperty("BaseUrl"));
		requestSpecBuilder.setBasePath(properties.getProperty("BasePath"));
		requestSpecBuilder.setContentType(properties.getProperty("ContentType"));
		requestSpec = requestSpecBuilder.build();	
		System.out.println("req spec"+requestSpec);

	}
}
