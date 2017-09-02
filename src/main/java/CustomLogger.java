package main.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomLogger {

	// Logging level
	//   0: error
	//   1: info + error
	//   2: warn + info + error
	//   3: debug + warn + info + error
	private static int loggingLevel = 3;
	
	// Print date in logs
	private static boolean printDate = false;
	
	// Separator content
	private static String separatorContent = "====================================================";
	
	/**
	 * Custom logger.
	 * 
	 * @param type Type of log.
	 * @param message Message to log.
	 */
	public static void log(String type, String message) {
		// Initialize message to log
		String logMessage = "[" + type + "] " + message;
		// Append date to the start, if required
		if(printDate) {
			// Get current date and time
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			// Append to logMessage
			logMessage = dateFormat.format(date) + " " + logMessage;
		}
		// Print message
		System.out.println(logMessage);
	}
	
	/**
	 * Log an error message.
	 * 
	 * @param message Message to log.
	 */
	public static void error(String message) {
		CustomLogger.log("error", message);
	}
	
	/**
	 * Log a success message.
	 * 
	 * @param message Message to log.
	 */
	public static void success(String message) {
		CustomLogger.log("success", message);
	}
	
	/**
	 * Log an info message.
	 * 
	 * @param message Message to log.
	 */
	public static void info(String message) {
		if(loggingLevel > 0) {
			CustomLogger.log("info", message);
		}
	}
	
	/**
	 * Log a warning message.
	 * 
	 * @param message Message to log.
	 */
	public static void warn(String message) {
		if(loggingLevel > 1) {
			CustomLogger.log("warn", message);
		}
	}
	
	/**
	 * Log a debug message.
	 * 
	 * @param message Message to log.
	 */
	public static void debug(String message) {
		if(loggingLevel > 2) {
			CustomLogger.log("debug", message);
		}
	}
	
	/**
	 * Logs a separator (see class attribute 'separatorContent')
	 */
	public static void separator() {
		if(loggingLevel > 0) {
			System.out.println(separatorContent);
		}
	}
}