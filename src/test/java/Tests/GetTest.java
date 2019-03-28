package Tests;

import org.testng.annotations.Test;
import utils.ReusableFunctions;

public class GetTest extends ReusableFunctions{
	
	String response; 
	
	@Test(priority=0)
	public void getRequest() {
		response = doGetRequest(properties.getProperty("SydneyID"), properties.getProperty("ApiKey"));
	}
	
	
	@Test(priority=1)
	public void getNumDaysWithTempabove(){	

		int days = getDayswithtemp(Double.parseDouble(properties.getProperty("TempAbovecels")), response);
        System.out.println("The number of days where the temperature is predicated to be above 20 degrees in the next 5 days = '"+days+"'");
        
	}
	
	@Test(priority=1)
	public void getNumDaysSunny() {

		int days = getDays("sunny",response);
        System.out.println("The number of sunny days in the next 5 days = '"+days+"'");
        
	}
	
	@Test(priority=1)
	public void getNumDaysCloudy() {
		
		int days = getDays("Clouds",response);
        System.out.println("The number of Cloudy days in the next 5 days = '"+days+"'");
	}
	
	@Test(priority=1)
	public void getNumDaysRainy() {
		
		int days = getDays("Rain",response);
        System.out.println("The number of Rainy days in the next 5 days = '"+days+"'");
	}

}
