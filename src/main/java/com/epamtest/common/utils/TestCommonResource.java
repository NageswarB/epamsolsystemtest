package com.epamtest.common.utils;

import java.io.File;

/**
 * 
 *@author nageswar.bodduri
 *This class is used to get the resources folder path under src/main/java
 */

public class TestCommonResource {

	private static final String SEPERATOR = File.separator;
	
	private static final String TEST_RESOURCE_DIR = System.getProperty("user.dir") + SEPERATOR + "src\\main\\java\\resources" + SEPERATOR;
	
	public static String getResourceDirectoryPath(){
		return TEST_RESOURCE_DIR;
	}
}
