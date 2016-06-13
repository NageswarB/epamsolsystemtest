package com.epamtest.selenium.webdriver;

public enum WebDriverType {

	FIREFOX_DRIVER("firefox"),
	INTERNET_EXPLORER_DRIVER("iexplorer"),
	CHROME_DRIVER("chrome");
	
	private String webDriverType;
	
	private WebDriverType(String type) {
		webDriverType = type;
	}
	
	public String getBrowserType(){
		return webDriverType;
	}
	
	public static WebDriverType getBrowserTypeFromString(String type){
		if(type != null){
			for(WebDriverType webDriverTypeItem: WebDriverType.values()){
				if( webDriverTypeItem.webDriverType.toLowerCase().equals(type.toLowerCase())){
					return webDriverTypeItem;
				}
			}
		}
		return null;
	}
	
}
