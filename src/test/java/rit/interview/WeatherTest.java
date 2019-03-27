package rit.interview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherTest{
	
	static RequestSpecification requestSpec;
	static RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
	static String responseAsString;
	static JsonPath jsonResponse;
	
	@BeforeClass
	public static void doGetRequest() {
		requestSpecBuilder.setBaseUri("http://api.openweathermap.org");
		requestSpecBuilder.setBasePath("/data/2.5/forecast");
		requestSpecBuilder.setContentType("application/json");
		requestSpec = requestSpecBuilder.build();	
		
		Response res =  RestAssured
				.given()
					.spec(requestSpec)
					.queryParam("id", "2147714") //city id
					.queryParam("APPID", "0f1fd25f7084fc4c9b61a658110b67c5") //API Key
				.when()
					.get()
				.then()
					.assertThat()
					.statusCode(200)
					.contentType(ContentType.JSON)
					.extract()
					.response();
		
	responseAsString = res.asString();
	jsonResponse = new JsonPath(responseAsString);
	//System.out.println("dogetrequest => "+responseAsString);	
	}
	
	
	@Test
	public void verifyCity() {
		Assert.assertEquals(true,responseAsString.contains("Sydney"));
	}
	
	
	@Test
	public void getNumDaysWithTemp20() throws ParseException {	

		Set<String> sDate = new HashSet<String>();
		int listSize = jsonResponse.getInt("list.size()");
		for(int i=0; i< listSize; i++) {
        	Double temp = jsonResponse.getDouble("list["+i+"].main.temp");
        	  if(temp-273.15 > 20.00) { 
				 String date = jsonResponse.getString("list["+i+"].dt_txt");
				 date = date.substring(0,10);
				 sDate.add(date);
			  }
        }
        System.out.println("The number of days where the temperature is predicated to be above 20 degrees in the next 5 days = '"+sDate.size()+"'");
        
	}
	
	@Test
	public void getNumDaysSunny() {
		Set<String> sunny = new HashSet<String>();
		int listSize = jsonResponse.getInt("list.size()");
		for(int i=0; i< listSize; i++) {
        	String mainweather = jsonResponse.getString("list["+i+"].weather[0].main");
        	  if(mainweather.equalsIgnoreCase("sunny")) { 
				  sunny.add(mainweather);
			}
        }
        System.out.println("The number of sunny days in the next 5 days = '"+sunny.size()+"'");
        
	}
}
