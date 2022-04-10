package me.deftware.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Manages the Minecraft launcher on Windows
 *
 * @author Deftware
 */
public class MinecraftLauncher {

	public static final File launcherBinary = new File("C:\\Program Files (x86)\\Minecraft Launcher\\MinecraftLauncher.exe");

	public static boolean isRunning() {
		try {
			return isProcessRunning("MinecraftLauncher.exe");
		} catch (Exception ex) {
			return false;
		}
	}

	public static void stop() {
		try {
			killProcess("MinecraftLauncher.exe");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void start() {
		try {
			if (launcherBinary.exists()) {
				Runtime.getRuntime().exec(launcherBinary.getAbsolutePath());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// https://stackoverflow.com/questions/81902/how-to-find-and-kill-running-win-processes-from-within-java
	public static boolean isProcessRunning(String serviceName) throws Exception {
		Process p = Runtime.getRuntime().exec("tasklist");
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains(serviceName)) {
				return true;
			}
		}
		return false;
	}

	public static void killProcess(String serviceName) throws Exception {
		Runtime.getRuntime().exec("taskkill /F /IM " + serviceName);
	}

}
