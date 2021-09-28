package test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Data.JsonReader;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import pages.Search;

public class SearchTest extends BaseTest {
	IOSDriver<IOSElement> driver;

	Search searchPage;

	@DataProvider(name = "SaerchData")
	public Object[][] passData() throws FileNotFoundException, IOException, ParseException {
		return JsonReader.getJSONData(System.getProperty("user.dir") + "/data/TestData.json", "Search", 1);

	}

	@Test(dataProvider = "SaerchData")
	public void search_for_trip(String departureName) {
		searchPage = new Search(driver);
		searchPage.enterDepartureLocation(departureName);

	}
}
