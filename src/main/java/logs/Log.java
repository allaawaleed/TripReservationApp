package logs;

import org.apache.log4j.Logger;

public class Log {

	private static String suffix = "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - ";
	private static String banner = "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
	private static String warning = "#######################";

	// Initialize Log4j instance
	private static Logger Log = Logger.getLogger(Log.class.getName());

	// We can use it when starting tests
	public static void startLog(String testClassName) {
		Log.info("Test is Starting...");
	}

	// We can use it when ending tests
	public static void endLog(String testClassName) {
		Log.info("Test is Ending...");
	}

	// Info Level Logs
	public static void info(String message) {
		Log.info(message);
	}

	// Warn Level Logs
	public static void warn(String message) {
		Log.warn(message);
	}

	// Error Level Logs
	public static void error(String message) {
		Log.error(message);
	}

	// Fatal Level Logs
	public static void fatal(String message) {
		Log.fatal(message);
	}

	// Debug Level Logs
	public static void debug(String message) {
		Log.debug(message);
	}

	public static void logAction(final String msg) {
		System.out.println("  " + msg);
		System.out.println("  " + suffix);
	}

	public static void logComment(final String msg) {
		System.out.println("        -> " + msg);
		System.out.println("  " + suffix);
	}

}
