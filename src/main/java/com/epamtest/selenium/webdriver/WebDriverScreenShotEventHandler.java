package com.epamtest.selenium.webdriver;

import java.io.File;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

import com.epamtest.common.utils.Log;
import com.google.common.io.Files;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

public class WebDriverScreenShotEventHandler implements WebDriverEventListener, TestListener{

	private WebDriver webDriver;
	private String path;
	protected boolean loaded = false;
	protected String screenShotPath = "screenshots";

	public WebDriverScreenShotEventHandler(){
		init();
	}

	private void init(){
		try{
			String fileDir = System.getProperty("user.dir") + File.separator + screenShotPath ;
			File dir = new File(fileDir);
			if(dir.exists()){
				loaded = true;
			}
			else if((dir).mkdir()) {
				Log.info("Create Directory : " + fileDir + " created !!");
				loaded = true;
			}
			else if(!(dir.exists())){
				Log.info("Failed to create directory " + fileDir);
			}
			path = fileDir;

		}catch(Exception e){
			Log.error("Failed to set screenshot file directory");
			loaded = false;
		}
	}

	/**
	 * Here implementing only onException method to capture screen shot of the applciation
	 */


	public void addError(Test arg0, Throwable arg1) {
		// TODO Auto-generated method stub

	}

	public void addFailure(Test arg0, AssertionFailedError arg1) {
		// TODO Auto-generated method stub

	}

	public void endTest(Test arg0) {
		// TODO Auto-generated method stub

	}

	public void startTest(Test arg0) {
		// TODO Auto-generated method stub

	}

	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub

	}

	public void afterNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	public void afterNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	public void afterNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	public void afterNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void afterScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub

	}

	public void beforeNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	public void beforeNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	public void beforeNavigateRefresh(WebDriver arg0) {
		// TODO Auto-generated method stub

	}

	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void beforeScript(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	public void onException(Throwable arg0, WebDriver arg1) {
		if(! (arg0 instanceof NoSuchElementException) &&
				!arg0.toString().contains("Element not found in the cache")){
			takeScreenshot(arg1, "Onexception-screenshot");
		}
	}

	/**
	 * This funciton will take screen shot 
	 * @param driver - the active web driver object
	 * @param title - title in the html report
	 * @return true in case of success otherwise false
	 */
	public boolean takeScreenshot(WebDriver driver, String title){

		if ( driver == null){
			driver = getDriver();
		}
		synchronized (driver) {
			boolean screenshot = false;
			try{
				if(loaded){
					File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					scrFile	 = copyFileToScreenshotFolder(scrFile);
					screenshot = true;
				}
			}catch(Throwable e){
				Log.error("Error in taking screen shot " + e.getMessage());
			}
			return screenshot;
		}
	}

	private File copyFileToScreenshotFolder(File file){
		File dest = null;
		if( file != null){
			try{
				dest = new File(path + File.separator + System.currentTimeMillis() + ".png");
				Files.copy(file, dest);
				file.delete();
			}catch(Exception e){

			}
		}
		return dest;
	}


	public WebDriver getDriver() {
		return webDriver;
	}

	public void setDriver(WebDriver driver) {
		this.webDriver = driver;
	}



}
