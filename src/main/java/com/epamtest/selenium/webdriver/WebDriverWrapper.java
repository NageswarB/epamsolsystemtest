package com.epamtest.selenium.webdriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class WebDriverWrapper extends EventFiringWebDriver {

	private WebDriverEventListener webDriverReportEventHandler;
	private WebDriver webDriver;
	private List<WebDriverEventListener> webDriverEventListeners;

	public WebDriverWrapper(WebDriver driver,WebDriverEventListener...webDriverEventHandlers){
		this(driver);
		webDriver = driver;
		if(webDriverEventHandlers != null){
			for(WebDriverEventListener webDriverEventHandler: webDriverEventHandlers){
				this.register(webDriverEventHandler);
			}
		}
	}

	public WebDriverWrapper(WebDriver driver) {
		super(driver);
		webDriver = driver;
		webDriverEventListeners = new ArrayList<WebDriverEventListener>();
	}

	@Override
	public EventFiringWebDriver register(WebDriverEventListener eventListener){
		webDriverEventListeners.add(eventListener);
		return super.register(eventListener);
	}

	@Override
	public EventFiringWebDriver unregister(WebDriverEventListener eventListener){
		webDriverEventListeners.remove(eventListener);
		return super.unregister(eventListener);
	}

	public WebDriver getDriver(){
		return this;
	}

	@Override
	public List<WebElement> findElements(By by){
		synchronized (webDriver) {
			return super.findElements(by);
		}
	}

	@Override
	public WebElement findElement(By by){
		synchronized (webDriver) {
			return super.findElement(by);
		}
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target){
		synchronized (webDriver) {
			return super.getScreenshotAs(target);
		}
	}

	@Override
	public Object executeScript(String script,Object...args){
		synchronized (webDriver) {
			return super.executeScript(script, args);
		}
	}

	@Override
	public Object executeAsyncScript(String script,Object...args){
		synchronized (webDriver) {
			return super.executeAsyncScript(script, args);
		}
	}

	@Override
	public void close(){
		for(WebDriverEventListener eventListener:webDriverEventListeners){
			//dispatch beforeClose()
			if(eventListener instanceof WebDriverCloseAndQuitEventListener){
				((WebDriverCloseAndQuitEventListener) eventListener).beforeClose();
			}
			try{
				super.close();
			}catch(UnreachableBrowserException ube){
				System.out.println("Got UnreachableBrowserException while trying to close the browser...there is no way to handle this issue;");
			}

			//dispatch afterClose()
			if(eventListener instanceof WebDriverCloseAndQuitEventListener){
				((WebDriverCloseAndQuitEventListener) eventListener).afterClose();
			}
		}
	}

	@Override
	public void quit(){
		for(WebDriverEventListener eventListener:webDriverEventListeners){
			//dispatch beforeQuit()
			if(eventListener instanceof WebDriverCloseAndQuitEventListener){
				((WebDriverCloseAndQuitEventListener) eventListener).beforeQuit();
			}

			super.quit();

			//dispatch afterQuit()
			if(eventListener instanceof WebDriverCloseAndQuitEventListener){
				((WebDriverCloseAndQuitEventListener) eventListener).afterQuit();
			}
		}
	}
}
