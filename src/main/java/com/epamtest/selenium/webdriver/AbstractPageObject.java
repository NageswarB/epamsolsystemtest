package com.epamtest.selenium.webdriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.epamtest.common.utils.Log;
import com.google.common.base.Function;

/**
 * 
 * @author nageswar.bodduri
 * This class contains all reusable methods and driver instantiate calls 
 *
 */
public class AbstractPageObject {

	protected static WebDriver driver;
	protected static boolean isBrowserOpen = false;
	protected String webDriverEventListenerClasses = "com.epamtest.selenium.webdriver.WebDriverScreenShotEventHandler";
	private String seleniumTimeOut = "30000";
	private String pageLoadTimeout = "180000";
	protected static final long DefaultTimeOutInSeconds = 30;
	protected static Properties SUTprop;


	static{
		loadEnvironmentProps();
	}

	public AbstractPageObject(){
		initElements();
	}

	void initElements(){
		if(driver == null){
			openBrowser();
		}
	}

	public static void loadEnvironmentProps(){
		SUTprop = new Properties();
		String workDir = System.getProperty("user.dir");

		File f=new File(workDir + "/environment.properties");

		if( f.exists()){
			Log.info(workDir + " is exists!!");
			try{
				SUTprop.load(new FileInputStream(f));
			}catch(FileNotFoundException fnfe){
				fnfe.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			Log.error(f.getName() + " not found !!!");
		}
	}

	/**
	 * This method is used to create browser object based on the browser type set in env properties
	 */
	protected void openBrowser(){

		if (!isBrowserOpen){
			try{
				WebDriverWrapper driverWrapper = getBrowserInstance();
				isBrowserOpen = true;
				initWebDriverEventListener(driverWrapper);

				if( seleniumTimeOut != null ){
					setImplicitWait(driverWrapper, seleniumTimeOut);
					setPageLoadTimeOut(driverWrapper, pageLoadTimeout);
				}

				driver = driverWrapper;
				driver.manage().window().maximize();

			}catch(Exception ex){
				Log.error("Exception while opening a browser " + ex.getMessage());
			}
		}

	}

	private WebDriverWrapper getBrowserInstance()throws Exception{

		WebDriverType wdType = null;
		String sWebDriverType = SUTprop.getProperty("browser");
		WebDriverWrapper webDriverInstance = null;
		wdType = setWebDriverType(sWebDriverType);
		webDriverInstance = WebDriverFactory.getWebDriver(wdType);
		return webDriverInstance;

	}

	private WebDriverType setWebDriverType(String browsertype) throws Exception{

		WebDriverType wdType = null;

		if( browsertype != null){
			if(browsertype.equalsIgnoreCase("chrome")){
				wdType = WebDriverType.CHROME_DRIVER;
			}else if(browsertype.equalsIgnoreCase("iexplorer")){
				wdType = WebDriverType.INTERNET_EXPLORER_DRIVER;
			}else{
				wdType = WebDriverType.FIREFOX_DRIVER;
			}
		}
		return wdType;
	}
	
	
	private void initWebDriverEventListener(WebDriverWrapper driverWrapper){
		if(webDriverEventListenerClasses != null && !webDriverEventListenerClasses.isEmpty()){
			//if you have more than one event listner
			String[] listeners = webDriverEventListenerClasses.split(";");

			ClassLoader loader;
			loader = this.getClass().getClassLoader();

			for(String eventListenerClass : listeners){
				try {
					if( eventListenerClass != null && !eventListenerClass.isEmpty()){
						eventListenerClass = eventListenerClass.trim();
						Class<?> loadClass = loader.loadClass(eventListenerClass);
						WebDriverEventListener webDriverEventListener = (WebDriverEventListener) loadClass.newInstance();
						driverWrapper.register(webDriverEventListener);
					}
				}catch(Throwable e){
					Log.error(eventListenerClass + " can not be loaded " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	private void setImplicitWait(WebDriverWrapper webDriverWrapper, String seleniumTimeOut){
		try{
			long miliSecs = Long.valueOf(seleniumTimeOut);
			Log.info("Set webdriver implicitlyWait seleniumTimeOut to " + seleniumTimeOut + "(MilliSeconds)");
			webDriverWrapper.getDriver().manage().timeouts().implicitlyWait(miliSecs, TimeUnit.SECONDS);
		}catch(Exception e){
			Log.error("Unable to set implicitly wait!!");
		}
	}

	private void setPageLoadTimeOut(WebDriverWrapper webDriverWrapper, String pageLodTimeout){
		try{
			long miliSecs = Long.valueOf(pageLodTimeout);
			Log.info("Set webdriver pageLoadTimeOut to " + pageLodTimeout + "(MilliSeconds)");
			webDriverWrapper.getDriver().manage().timeouts().implicitlyWait(miliSecs, TimeUnit.SECONDS);
		}catch(Exception e){
			Log.error("Unable to set implicitly wait!!");
		}
	}

	/**###
	 * #commonly used web application actions
	 * ###
	 */

	public void typeEditBox(String editBoxName, String textToType) throws Exception{

		String xpath = "//input[@id='" + editBoxName + "' or " +
				"@name='" + editBoxName + "']";

		waitForElementVisibility(By.xpath(xpath));
		WebElement editBox = driver.findElement(By.xpath(xpath));
		editBox.sendKeys(textToType);
	}

	public static void clickButton(String btnName) throws Exception{

		String btnXpath = "//input[@value='" + btnName + "' or " +
				"//input[@type='" + btnName + "']";
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, 30);

		try{
			element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(btnXpath)));
			element.click();
		}catch(TimeoutException tx){
			//
		}

	}

	public WebElement waitForElementVisibility(By by){
		return waitForElementVisibility(by, DefaultTimeOutInSeconds);
	}

	public WebElement waitForElementVisibility(By by, long timeOutInSeconds){

		WebElement webElement = null;
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		try{
			webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		}catch(TimeoutException ex){
			//to do things
		}

		if(webElement == null)
			Log.warn("Waited for " + timeOutInSeconds + " seconds for the webelement visibility...but not found");		
		return webElement;
	}

	/**
	 * This method is used to wait for an element until it is visible in the web page
	 * Used fluent wait to return the control right after the element visibility 
	 * @param by
	 * @return web element
	 */
	public WebElement waitUntilElementVisibility(final By by){

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(60, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		WebElement we = wait.until(new Function<WebDriver, WebElement>() {

			public WebElement apply(WebDriver driver){
				return driver.findElement(by);
			}
		});
		return we;
	}
}
