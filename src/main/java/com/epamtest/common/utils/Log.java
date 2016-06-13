package com.epamtest.common.utils;

import org.apache.log4j.Logger;

/**
 * 
 * @author nageswar.bodduri
 * This class is used to capture the events occurs during test run
 */

public class Log {

	private static Logger log = Logger.getLogger(Log.class.getName());
	
	public static void info(String message){
		log.info(message);
	}
	
	public static void warn(String message){
		log.warn(message);
	}
	
	public static void error(String message){
		log.error(message);
	}
}