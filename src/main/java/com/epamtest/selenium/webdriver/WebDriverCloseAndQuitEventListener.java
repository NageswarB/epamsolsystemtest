package com.epamtest.selenium.webdriver;

import org.openqa.selenium.support.events.WebDriverEventListener;

public interface WebDriverCloseAndQuitEventListener extends WebDriverEventListener {

	public void beforeQuit();
	
	public void afterQuit();
	
	public void beforeClose();
	
	public void afterClose();

}
