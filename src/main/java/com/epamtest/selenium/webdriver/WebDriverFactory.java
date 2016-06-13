package com.epamtest.selenium.webdriver;

import java.util.ArrayList;

import javax.management.DescriptorRead;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.epamtest.common.utils.Log;
import com.epamtest.common.utils.TestCommonResource;

public class WebDriverFactory {

	private static String chromeProfile = null;
	private static String chromeExtension = null;
	private static String firefoxProfile = null;
	private static String firefoxExtension = null;
	private static String firefoxBrowserPath;
	private static boolean windowMaximize = true;

	public static WebDriverWrapper getWebDriver(WebDriverType wdType) throws Exception{
		return webDriverFactory(wdType);
	}

	/**
	 * This method is used to create webdriver instance
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private static WebDriverWrapper webDriverFactory(WebDriverType type) throws Exception{

		WebDriverWrapper driver = null;
		WebDriver webDriver = null;

		try{
			if(type == null){
				throw new IllegalArgumentException(
						"the input browser type is null (change the value of the 'browser' type in environment properties");
			}

			switch(type){
			case CHROME_DRIVER:
				webDriver = getChromeDriver();
				break;

			case FIREFOX_DRIVER:
				webDriver = getFirefoxDriver();
				break;

			case INTERNET_EXPLORER_DRIVER:
				webDriver = getInternetExplorerDriver();
				break;

			default:
				throw new IllegalArgumentException("browser type is unsupported");
			}

		}catch(IllegalArgumentException iae){
			Log.error(iae.getMessage());
			iae.printStackTrace();
			throw iae;
		}
		if(webDriver == null){
			Log.error("Failed to init the web driver");
			throw new IllegalArgumentException("Failed to init the web driver - " + type);
		}else{
			driver = new WebDriverWrapper(webDriver);
		}
		return driver;
	}

	protected static WebDriver getChromeDriver(){

		WebDriver driver = null;
		ChromeOptions options = new ChromeOptions();

		String chromeDriverExePath = TestCommonResource.getResourceDirectoryPath() + "webdriver\\chromedriver.exe";

		try{
			Log.info("The Chrome Driver Path is = " + chromeDriverExePath);
			System.setProperty("webdriver.chrome.driver", chromeDriverExePath);

			ArrayList<String> switches = new ArrayList<String>();

			if( chromeProfile != null){
				Log.info("Open web driver chrome with the profile (" + chromeProfile + ")");
				switches.add("--user-data-dir=" + chromeProfile);
			}

			if( chromeExtension != null){
				Log.info("Open web driver chrome with the chrome extension (" + chromeExtension + ")");
				switches.add("--load-extension=" + chromeExtension);
			}

			if( windowMaximize == true){
				Log.info("open webdriver chrome with the flag of maximize");
				switches.add("--start-maximized");
			}

			if( switches.size() > 0){
				options.addArguments(switches);
				driver = new ChromeDriver(options);
			}else{
				driver = new ChromeDriver();
			}
		}catch(Throwable e){
			e.printStackTrace();
		}

		return driver;
	}

	protected static WebDriver getInternetExplorerDriver(){

		WebDriver webDriver = null;
		String IEDriverExePath = TestCommonResource.getResourceDirectoryPath() + "webdriver\\IEDriverServer.exe";
		try{
			System.setProperty("webdriver.ie.driver", IEDriverExePath);

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability("nativeEvents", true);
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			webDriver = new InternetExplorerDriver(ieCapabilities);
		}catch(Exception e){
			Log.error("Exception occured while creating ie driver");
			e.printStackTrace();
		}
		return webDriver;
	}
	
	protected static WebDriver getFirefoxDriver(){
		
		WebDriver webDriver = null;
		
		try{
			webDriver = new FirefoxDriver();
		}catch(Exception e){
			Log.error("Exception occured while creating firefox driver object");
			e.printStackTrace();
		}
		return webDriver;
	}
}
