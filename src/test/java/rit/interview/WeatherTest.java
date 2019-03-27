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
	System.out.println("dogetrequest => "+responseAsString);	
	}
	
	
	@Test
	public void verifyCity() {
		Assert.assertEquals(true,responseAsString.contains("Sydney"));
	}
	
	
	@Test
	public void getNumDaysWithTemp20() throws ParseException {	

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Set<Date> lDate = new HashSet<Date>();
		Date dt = new Date();
		int listSize = jsonResponse.getInt("list.size()");
		
        System.out.println(listSize);
        System.out.println(lDate);
        for(int i=0; i< listSize; i++) {
        	Double temp = jsonResponse.getDouble("list["+i+"].main.temp");
        	System.out.println("number "+ i +" value "+temp);
			  if(temp-273.15 > 20.00) { 
				  System.out.println("in for loop "+i);
				  System.out.println(jsonResponse.getString("list["+i+"].dt_txt"));
				 dt = formatter.parse(jsonResponse.getString("list["+i+"].dt_txt")); 
				  }
			 
			  //List<Date> date = new SimpleDateFormat("dd/MM/yyyy").parse(lDate);
        }
        System.out.println(lDate);
        
	}
	
	private Date parse(String string) {
		// TODO Auto-generated method stub
		return null;
	}


	@Test
	public void getNumDaysSunny() {
		
	}
}
