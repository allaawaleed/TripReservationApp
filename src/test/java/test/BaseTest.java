package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import logs.Log;

public class BaseTest {

	private static AppiumDriver driver;
	private static WebDriverWait driverWait;

	private static DesiredCapabilities capabilities;

	private static String packageName;
	private static String mainActivity = "com.google.android.apps.chrome.Main";

	private static final String CONTEXT_NATIVE_APP = "NATIVE_APP";
	private static final String CONTEXT_CHROMIUM = "CHROMIUM";
	private static String CONTEXT_WEBVIEW;

	public static boolean nativeContext;

	private static JSONObject runConfigJson;
	private static final String RUN_CONFIG_FILENAME = "/runConfig.json";

	private static String PLATFORM_NAME;
	private static String APPIUM_PORT_NUMBER_DEFAULT;
	private static String APPIUM_SERVER_ADDRESS_DEFAULT;

	private static final String iOS_AUTOMATION_NAME = "xcuitest";
	private static final String APPIUM_COMMAND = "appium";

	private static String platform;
	private static String deviceName;
	private static String osVersion;
	private static String appPath;
	private static String appiumPort;

	public static final String iOS_PLATFORM = "iOS";
	public static final String ANDROID_PLATFORM = "Android";
	public static final String iPHONE = "iPhone";
	public static final String iPAD = "iPad";

	public static int deviceScreenHeight;
	public static int deviceScreenWidth;

	/**
	 * @noReset essentially should only be used if you're intentionally making two
	 *          tests dependent on one another
	 */
	protected static boolean noReset = false;

	/**
	 * @fullReset is mutually exclusive from noRest. So should be true when noReset
	 *            = false.
	 */
	protected static boolean fullReset = true;

	@BeforeClass
	public static void setUp() throws Exception {

		// Set run configurations
		initializeRunConfigurationSettings();

		try {
			PLATFORM_NAME = getPlatform();
			APPIUM_PORT_NUMBER_DEFAULT = getAppiumPort();
			APPIUM_SERVER_ADDRESS_DEFAULT = "http://localhost:" + APPIUM_PORT_NUMBER_DEFAULT + "/wd/hub";

			if (isAndroid()) {
				// Setting Android capabilities
				capabilities = DesiredCapabilities.android();

				capabilities.setCapability("app-package", packageName);
				capabilities.setCapability("app-activity", mainActivity);

			} else {
				// Setting iOS capabilities
				capabilities = DesiredCapabilities.iphone();
				capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, iOS_AUTOMATION_NAME);

				// For now the reset capabilities are changeble only for iOS. However, depending

				capabilities.setCapability(MobileCapabilityType.NO_RESET, noReset);
				capabilities.setCapability(MobileCapabilityType.FULL_RESET, fullReset);

				// need this capability to start the ios-webkit-debug-proxy
				capabilities.setCapability("startIWDP", true);
				capabilities.setCapability("bundleId", packageName);

				if (isDevice()) {
					capabilities.setCapability(MobileCapabilityType.UDID, getUDID());
				}
			}

			// Capabilities common to both iOS and Android platforms
			capabilities.setCapability(MobileCapabilityType.PLATFORM, getPlatform());
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PLATFORM_NAME);

			// Add capability of AVD if emulator else we consider it as device
			if (isEmulator()) {
				capabilities.setCapability("avd", getDeviceName());
			} else {
				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, getDeviceName());
			}
			capabilities.setCapability(MobileCapabilityType.VERSION, getOSVersion());
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 600);

			startAppiumServer(APPIUM_PORT_NUMBER_DEFAULT);

			/*
			 * initialize driver
			 */
			if (isAndroid()) {
				setDriver(new AndroidDriver(new URL(APPIUM_SERVER_ADDRESS_DEFAULT), capabilities));
			} else {
				setDriver(new IOSDriver(new URL(APPIUM_SERVER_ADDRESS_DEFAULT), capabilities));
			}

			// Reset app to ensure tests start afresh.
			if (noReset) {
				getDriver().resetApp();
			}
		} catch (Exception ex) {
			throw new Exception(
					String.format("Exception caught within BaseTest.setup(). Message: %s", ex.getMessage()));
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		try {
			getDriver().quit();
			stopAppiumServer(APPIUM_PORT_NUMBER_DEFAULT);
		} catch (Exception ex) {
			throw new Exception(String.format(
					"Depending on your device and timing, the driver might already have closed: %s", ex.getMessage()));
		}
	}

	// Initialize the application path for use within Base class and example

	protected static void initializeRunConfigurationSettings() throws Exception {
		setPlatform();
		setOSVersion();
		setDeviceName();
		setPackageName();
		setAppPath();
		setAppiumPort();
	}

	// Read example configuration setting from example config file.
	private static String readConfigSetting(String keyName) throws JSONException {
		String testConfigValue = (String) runConfigJson.get(keyName);
		Log.logComment(String.format("Test config lookup for key: %s, returning value: %s", keyName, testConfigValue));
		return testConfigValue;
	}

	/**
	 * Set the OS platform e.g Android or iOS
	 * 
	 * @throws Exception
	 */
	private static void setPlatform() throws Exception {
		String platformFromConfig = readConfigSetting("platform");
		if (platformFromConfig.equalsIgnoreCase(ANDROID_PLATFORM)) {
			Log.logAction("*** Test script will run in 'Android' mode ***");
			platform = ANDROID_PLATFORM;
		} else if (platformFromConfig.equalsIgnoreCase(iOS_PLATFORM)) {
			Log.logAction("*** Test script will run in 'iOS' mode ***");
			platform = iOS_PLATFORM;
		} else {
			throw new Exception(String.format("We did not recognize the platform %s.", platformFromConfig));
		}
	}

	// Sets the OS version of platform under execution
	private static void setOSVersion() throws Exception {
		osVersion = readConfigSetting("device_version_" + getPlatform().toLowerCase());
	}

	// Sets the device name of platform under execution
	private static void setDeviceName() throws Exception {
		deviceName = readConfigSetting("device_name_" + getPlatform().toLowerCase());
	}

	// Sets the device package name for Android and bundle id for iOS
	private static void setPackageName() throws Exception {
		packageName = readConfigSetting("package_name");
	}

	// Sets the app path on machine
	private static void setAppPath() throws Exception {
		String path;
		if (isAndroid()) {
			path = readConfigSetting("androidAppPackagePath");
		} else if (isiOS()) {
			if (isiOSSimluator()) {
				path = readConfigSetting("iosSimulatorAppPackagePath");
			} else {
				path = readConfigSetting("iosDeviceAppPackagePath");
			}
		} else {
			throw new Exception("Cannot identify Platform type to get correct app path");
		}
		appPath = path;
	}

	// To set Appium port on which server would start
	private static void setAppiumPort() throws Exception {
		appiumPort = readConfigSetting("default_appium_port_number");
	}

	// Gets package name of app
	public static String getPackageName() {
		return packageName;
	}

	// Gets the package + id portion of resource ID
	public static String getResourceID() {
		return getPackageName() + ":id/";
	}

	// Gets path to app installable file
	public static String getAppPath() throws Exception {
		return appPath;
	}

	// Gets platform that is used to run tests
	public static String getPlatform() {
		return platform;
	}

	// Gets OS version of platform that is used to run tests
	public static String getOSVersion() {
		return osVersion;
	}

	// Gets Device name used to run tests
	public static String getDeviceName() {
		return deviceName;
	}

	// Helper method to get 'target' from run config file
	private static String getTarget() throws Exception {
		return readConfigSetting("target");
	}

	// To get Appium port number on which the server starts
	private static String getAppiumPort() throws Exception {
		return appiumPort;
	}

	// Gets iOS device UDID
	public static String getUDID() throws Exception {
		if (isiOS()) {
			String UDID = readConfigSetting("udid");
			if (UDID.equals("")) {
				throw new Exception("UDID in config file is empty");
			} else {
				return UDID;
			}
		} else {
			throw new Exception("UDID is only for iOS cannot get it for: " + getPlatform());
		}
	}

	// Checks to see if this is an Android device.
	public static boolean isAndroid() {
		return getPlatform().equals(ANDROID_PLATFORM);
	}

	// Checks to see if this is an iOS device.
	public static boolean isiOS() {
		return getPlatform().equals(iOS_PLATFORM);
	}

	// Check if we are using a simulator
	public static boolean isiOSSimluator() throws Exception {
		return getTarget().equalsIgnoreCase("simulator");
	}

	// Check if we are using an android emulator
	public static boolean isEmulator() throws Exception {
		return getTarget().equalsIgnoreCase("emulator");
	}

	// Check if we are using a device to run tests
	public static boolean isDevice() throws Exception {
		return getTarget().equalsIgnoreCase("device");
	}

	// To start Appium server
	private static void startAppiumServer(String port) throws Exception {
		Log.logAction(String.format("Starting Appium server on port %s", port));
		if (!isAppiumServerRunning(port)) {
			// command to start Appium server --> appium -p 4273
			Log.logComment("Starting Server");
			try {
				Log.logComment("Appium server started with");
			} catch (Exception serverNotStarted) {
				Log.warn("Could not start Appium Server");
				throw new Exception(serverNotStarted.getMessage());
			}
		} else {
			Log.logComment("Appium server already started");
		}
	}

	// To execute a terminal command, and get the complete log response.
	public static String runCommandAndWaitToComplete(String[] command) throws Exception {
		String completeCommand = String.join(" ", command);
		Log.logAction("Executing command: " + completeCommand);
		String line;
		String returnValue = "";

		try {
			Process processCommand = Runtime.getRuntime().exec(command);
			BufferedReader response = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));

			try {
				processCommand.waitFor();
			} catch (InterruptedException commandInterrupted) {
				throw new Exception("Were waiting for process to end but something interrupted it"
						+ commandInterrupted.getMessage());
			}

			while ((line = response.readLine()) != null) {
				returnValue = returnValue + line + "\n";
			}

			response.close();

		} catch (Exception e) {
			throw new Exception("Unable to run command: " + completeCommand + ". Error: " + e.getMessage());
		}

		Log.logComment("Response : runCMDAndWaitToComplete(" + completeCommand + ") : " + returnValue);
		return returnValue;
	}

	// To check if Appium server is already up and running on the desired port
	private static boolean isAppiumServerRunning(String port) throws Exception {
		Log.logAction(String.format("Checking if Appium server is executing on port %s", port));

		// command to check if Appium service running on port --> sh -c lsof -P | grep
		// ':4723'
		String checkCommand[] = new String[] { "sh", "-c", String.format("lsof -P | grep :%s", port) };
		if (runCommandAndWaitToComplete(checkCommand).equals("")) {
			Log.warn(String.format("Appium server is not running on port %s", port));
			return false;
		} else {
			Log.logComment(String.format("Appium server is running on port %s", port));
			return true;
		}
	}

	// To stop appium server
	private static void stopAppiumServer(String port) throws Exception {
		Log.logAction(String.format("Stopping Appium server on port %s", port));
		// command to stop Appium service running on port --> sh -c lsof -P | grep
		// ':4723' | awk '{print $2}' | xargs kill -9
		String stopCommand[] = new String[] { "sh", "-c",
				String.format("lsof -P | grep ':%s' | awk '{print $2}' | xargs kill -9", port) };
		runCommandAndWaitToComplete(stopCommand);
	}

	public static AppiumDriver getDriver() {
		return driver;
	}

	public static void setDriver(AppiumDriver driver) {
		BaseTest.driver = driver;
	}

}
