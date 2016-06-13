package com.epamtest.tests;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.epamtest.common.utils.Log;
import com.epamtest.testimpl.SourceForgeDownloadsPage;

public class VerifyVersionNumberTest {

	public static SourceForgeDownloadsPage sfpage;

	@BeforeTest
	public void setup(){
		sfpage = new SourceForgeDownloadsPage();
	}

	@Test
	public void verifyMantisBTVersion() throws Exception {
		
		String expectedMantisBTVersion = "1.2.19";
		String actualMantisBTVersion = sfpage.getLatestMantisBTVersionFromSourgeForgePage();
		Assert.assertEquals(actualMantisBTVersion, expectedMantisBTVersion);
		Log.info("MantisBT version which has the most download hits is " + actualMantisBTVersion);
		
	}

	@AfterTest
	public void tearDown(){
		sfpage.quit();
	}
}
