
**Description**
-----
* This project is intial framework built in Page Object Model style using TestNG framework to test
    Trip reservation Native app to test android and iOS app using appium
 * Project consists of many packages as the following:
   * Data: This should contain all data driven needed using JSON
   * Device Config: To add Android and iOS configuration 
   * Pages: This contains all test cases in the separate classes.
   * test: This should contain test classes to be able to run tests in parell
   * Logs: This contains the logging class.
   
**Features**
-----
* Easy to automate any type of application
* Page Object Model
* TestNG integration


**Libraries/Plugins used**
-------------------
* Selenium: To initiate the driver and deal with the web elements.
* Appium: Open-source framework for mobile app automation testing.
* Log4j: Used for logging.
* Testng: Used for test annotations, asserting on the results and parallel execution. 
* Maven-surefire-plugin: used for configuring the suite XML and parametrised variables through the command line.
* Maven-compiler-plugin: used to compile the project to version 1.8 because it will not work for 1.5 because of multiple catch exceptions.


**How to run?**
----
 * Still need to work on it but as a Search samle
 * Run the 'testNG.xml' as 'TestNG' suite. 
 * If compiler display error 'Failed to execute goal' or 'Failed to execute classpath' please remove all files in .m2/repository , restart Eclipse, update Maven with selecting force, Ran as clean Maven, Run as Maven Install, Clean project then Run project.

The solution includes:
* Logging using log4j;
* Report will be in the test-output folder after running the test;


