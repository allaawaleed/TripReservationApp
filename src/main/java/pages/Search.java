package pages;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSBy;

public class Search extends BasePage {

	public Search(IOSDriver<IOSElement> driver) {
		super(driver);
	}

	// Define locaters after connect ips

	@iOSBy(xpath = "")
	private MobileElement departureLocation;

	@iOSBy(id = "")
	private MobileElement destinationField;

	@iOSBy(id = "")
	private MobileElement departureDate;

	@iOSBy(id = "")
	private MobileElement returnDate;

	@iOSBy(id = "")
	private MobileElement passengerField;

	@iOSBy(id = "")
	private MobileElement passengerList;

	@iOSBy(id = "")
	private MobileElement searchButton;

	public void enterDepartureLocation(String departureName) {
		clear(departureLocation);
		sendText(departureLocation, departureName);

	}

	public void enterDestinationLocation() {
		clear(destinationField);
		sendText(destinationField, "");

	}

	public void enterDedepartureDate() {
		clear(departureDate);
		sendText(departureDate, "");

	}

}
