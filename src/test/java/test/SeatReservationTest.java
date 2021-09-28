package test;

import org.testng.annotations.Test;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import pages.Search;

public class SeatReservationTest extends BaseTest {
	IOSDriver<IOSElement> driver;

	Search searchPage;

	@Test(description = "Verify that user is able to reserve an avaiable seat of his choice")
	public void selectValidSeat() {

	}

}
