package telemServer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import common.Log;

public class ServerConfig {
	public static Properties properties; // Java properties file for user defined values
	public static final String propertiesFileName = "FoxTelemServer.properties";
	public static boolean slowSpeedRsDecode=true;
	public static boolean highSpeedRsDecode=true;

	public static void init() {
		properties = new Properties();
		load();
	}
	public static void save() {
		properties.setProperty("slowSpeedRsDecode", Boolean.toString(slowSpeedRsDecode));
		properties.setProperty("highSpeedRsDecode", Boolean.toString(highSpeedRsDecode));
		store();
	}

	private static void store() {
		try {
			FileOutputStream fos = new FileOutputStream(propertiesFileName);
			properties.store(fos, "Fox 1 Telemetry Decoder Properties");
			fos.close();
		} catch (FileNotFoundException e1) {
			Log.errorDialog("ERROR", "Could not write properties file. Check permissions on directory or on the file\n" +
					propertiesFileName);
			e1.printStackTrace(Log.getWriter());
		} catch (IOException e1) {
			Log.errorDialog("ERROR", "Error writing properties file");
			e1.printStackTrace(Log.getWriter());
		}

	}

	private static String getProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null) throw new NullPointerException();
		return value;
	}

	public static void load() {
		// try to load the properties from a file
		try {
			FileInputStream fis = new FileInputStream(propertiesFileName);
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			Log.println("Writing Default properties file");
			save();
		}
		try {
			slowSpeedRsDecode = Boolean.parseBoolean(getProperty("slowSpeedRsDecode"));
			highSpeedRsDecode = Boolean.parseBoolean(getProperty("highSpeedRsDecode"));

		} catch (NumberFormatException nf) {
			Log.println("FATAL: Could not load properties: " + nf.getMessage());
			System.exit(1);
		}
		catch (NullPointerException nf) {
			Log.println("FATAL: Could not load properties: " +nf.getMessage());
			System.exit(1);
		}
		Log.println("LOADED: " + propertiesFileName);
	}
}
