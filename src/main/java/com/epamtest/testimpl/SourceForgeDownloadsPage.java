package com.epamtest.testimpl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.epamtest.common.utils.Log;
import com.epamtest.selenium.webdriver.AbstractPageObject;

/**
 * This is the actual test implementation file ,contains UI driving code and validations
 * @author nageswar.bodduri
 * 
 *
 */
public class SourceForgeDownloadsPage extends AbstractPageObject{

	//this will call the AbstractPageObject C'tor which will initialize driver object
	public SourceForgeDownloadsPage() {
		super();
	}

	public String getLatestMantisBTVersionFromSourgeForgePage() throws Exception{

		WebElement googleSearchArea = null;	WebElement mantisStableLink = null;	WebElement filesLink = null;

		Log.info("**********************Test begin**************************");
		try{
			driver.get(SUTprop.getProperty("URL"));
			googleSearchArea = driver.findElement(By.name("q"));

			if(googleSearchArea.isEnabled()) {
				Log.info("successfully launched google search page");
				googleSearchArea.sendKeys("mantis");
				googleSearchArea.submit();
			}

			WebElement googleSearchResultPage = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.elementToBeClickable(By.id("resultStats")));

			clickSourceForgeLink(googleSearchResultPage);
			filesLink = waitUntilElementVisibility(By.linkText("Files"));

			if( filesLink != null){
				filesLink.click();
				Log.info("clicked Files link");
				mantisStableLink = waitUntilElementVisibility(By.linkText("mantis-stable"));
				mantisStableLink.click();
				Log.info("clicked mantis-stable link");
			}else{
				Log.warn("Files link is not visibile on SourceForge downloads page");
			}
		}catch(Exception e){
			Log.error("Exception occured while getting the Mantis BT software build version");
			e.printStackTrace();
		}

		return highestDownloadedBuildVersion();
	}

	//This method takes google search results page as input parameter and clicks on source forge link
	
	private void clickSourceForgeLink(WebElement we){
		
		String href = null;
		String expectedSOURCEURI = "https://sourceforge.net/projects/mantisbt/";

		try{
			if( we != null){
				List<WebElement> hyperlinks = driver.findElements(By.xpath("//*[@id='rso']//h3/a"));
				for(WebElement hyperlink: hyperlinks){
					href = hyperlink.getAttribute("href");
					if(href.equalsIgnoreCase(expectedSOURCEURI)){
						hyperlink.click();
						Log.info("clicked " + href + " link in google search page successfully!!!");
						break;
					}
				}
			}
		}catch(Exception e){
			Log.error("Exception occured while clicking on SourceForge website");
			e.printStackTrace();
		}
	}

	//This method does the validation by comparing download hits tuple by tuple in the File list table
	private String highestDownloadedBuildVersion(){

		String currentBuildVersion, previousBuildVersion = null;
		long currentHighestDownloadCount = 0, previousHighestDownloadCount = 0;
		String currentModifiedDate = null, previousModifiedDate = null;
		WebElement fileListTable;

		try{
			fileListTable = driver.findElement(By.id("files_list"));
			List<WebElement> rows = fileListTable.findElements(By.xpath("//table[@id='files_list']/tbody/tr"));

			Log.info("checking the download frequency of all builds of MantisBT s/w");
			for(WebElement row : rows){
				String rowData[] = row.getText().split(" ");
				currentBuildVersion = rowData[0].trim();
				currentModifiedDate = rowData[1].trim();
				try {
					currentHighestDownloadCount = (Long) NumberFormat.getNumberInstance(java.util.Locale.US).parse(rowData[2].trim());
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				//check whether current highest hit count is greater than the previous one
				//if yes keep the current highest in previous and continue with next record
				//if no simple go to next record
				if( currentHighestDownloadCount > previousHighestDownloadCount){
					previousBuildVersion = currentBuildVersion;
					previousModifiedDate = currentModifiedDate;
					previousHighestDownloadCount = currentHighestDownloadCount;
				}else{
					continue;
				}
				
			}
			Log.info("build version which has the most hits is : " + previousBuildVersion);
			Log.info("**********************Test finish**************************");
		}catch(Exception e){
			Log.error("Exception occured while finding the highest download version");
			e.printStackTrace();
		}
		return previousBuildVersion;
	}

	//close all open browser instances
	public void quit(){
		try{
			driver.quit();
			Log.info("closing all browser instances");
		}catch(Exception e){
			Log.error("exception occured while closing the browser");
		}
	}
} //End of the main class
