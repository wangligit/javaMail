package com.lora.mail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import sun.org.mozilla.javascript.internal.GeneratedClassLoader;

public class Configuration {
	private static final String CLASSNAME = Configuration.class.getSimpleName();

	static String mainACDntServer;

	static String backupACDntServer;

	static String mainACDshServer;

	static String backupACDshServer;

	private static Properties p;
	static {
		p = new Properties();
	/*	String path = getWebRoot() + File.separator + "WEB-INF"
				+ File.separator + "conf" + File.separator + "conf.properties";*/
		InputStream in = Configuration.class.getClassLoader().getResourceAsStream("conf.properties");
		try {
//			FileInputStream in = new FileInputStream(path);
			p.load(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getConfigValue(String name) {
		return p.getProperty(name);
	}

	@SuppressWarnings("unchecked")
	public static Enumeration<String> getConfigValuesEnum() {
		return (Enumeration<String>) p.propertyNames();
	}

	public static String getWebRoot() {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classLoader.getResource("/");
		String filepath = url.getPath();
		filepath = filepath.substring(0, filepath.lastIndexOf("/"));
		filepath = filepath.substring(0, filepath.lastIndexOf("/"));
		filepath = filepath.substring(0, filepath.lastIndexOf("/"));
		return filepath;
	}

	public static String getMainACDntServer() {
		return mainACDntServer;
	}

	public static void setMainACDntServer(String ip) {
		backupACDntServer = mainACDntServer;
		mainACDntServer = ip;
	}

	public static String getBackUpACDntServer() {
		return backupACDntServer;
	}

	public static String getMainACDshServer() {
		return mainACDshServer;
	}

	public static void setMainACDshServer(String ip) {
		backupACDshServer = mainACDshServer;
		mainACDshServer = ip;
	}

	public static String getBackUpACDshServer() {
		return backupACDshServer;
	}
}
