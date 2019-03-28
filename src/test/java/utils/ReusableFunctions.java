package utils;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.testng.annotations.BeforeSuite;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ReusableFunctions {
	
	 public Properties properties;
	 RequestSpecification requestSpec;
	 RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

	@BeforeSuite(alwaysRun=true)
	public void setUpConfig() {
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
	}
	
	public String doGetRequest(String cityid, String ApiKey) {
		Response response = RestAssured
								.given()
									.spec(requestSpec)
									.queryParam("id", cityid) //city id
									.queryParam("APPID", ApiKey) //API Key
								.when()
									.get();
								/*.then()
									.statusCode(200)
									.and()
									.contentType(ContentType.JSON)
									.extract()
									.response();*/
		//System.out.println("response == "+response.body().asString());
		return response.body().asString();
	}
	
	public int getListSize(String response) {
		JsonPath jresponse = new JsonPath(response);
		return jresponse.getInt("list.size()");
	}
	
	public int getDays(String value, String response) {
		Set<String> sDate = new HashSet<String>();
		JsonPath jresponse = new JsonPath(response);
		for(int i=0; i< getListSize(response); i++) {
        	String mainweather = jresponse.getString("list["+i+"].weather[0].main");
        	  if(mainweather.equalsIgnoreCase(value)) { 
        		 String date = jresponse.getString("list["+i+"].dt_txt");
 				 date = date.substring(0,10);
 				sDate.add(date);
			}
        }
		//System.out.println(sDate);
		return sDate.size();
	}
	
	public int getDayswithtemp(double temp, String response) {
		JsonPath jresponse = new JsonPath(response);
		Set<String> sDate = new HashSet<String>();
		
		for(int i=0; i< getListSize(response); i++) {
        	Double temp1 = jresponse.getDouble("list["+i+"].main.temp");
        	  if(temp1-273.15 > temp) { 
				 String date = jresponse.getString("list["+i+"].dt_txt");
				 date = date.substring(0,10);
				 sDate.add(date);
			  }
        }
		//System.out.println(sDate);
		return sDate.size();
	}


	
	
}
