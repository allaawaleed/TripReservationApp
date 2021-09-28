package pages;

import java.time.Duration;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class BasePage {
	public int IMPLICIT_WAIT;
	protected AppiumDriver<IOSElement> driver;
	long WAIT = 10;

	public BasePage(IOSDriver<IOSElement> driver2) {
		this.driver = driver2;
		initElements();
	}

	public void waitElementToBeVisible(MobileElement elmenet) {

		WebDriverWait wait = new WebDriverWait(driver, WAIT);
		wait.until(ExpectedConditions.visibilityOf(elmenet));
	}

	private void initElements() {
		PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(IMPLICIT_WAIT)), this);
	}

	public void clear(MobileElement mobileElement) {
		waitElementToBeVisible(mobileElement);
		mobileElement.clear();
	}

	public void click(MobileElement mobileElement) {
		waitElementToBeVisible(mobileElement);
		mobileElement.click();
	}

	public void sendText(MobileElement mobileElement, String text) {
		waitElementToBeVisible(mobileElement);
		mobileElement.sendKeys(text);
	}

}
