package me.deftware.installer.engine;

import me.deftware.installer.LocationUtil;
import me.deftware.installer.Main;
import me.deftware.installer.OSUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Manages LWJGL natives
 *
 * @author Deftware
 */
public class NativeManager {

	public static HashMap<String, File> natives = new HashMap<>();

	/**
	 * Extracts natives from this jar
	 */
	public static void extractNatives() {
		LocationUtil self = LocationUtil.getClassPhysicalLocation(Main.class);
		if (self.toFile() != null && self.toFile().exists()) {
			try {
				File nativesDirectory = getNativesDirectory();
				JarFile jar = new JarFile(self.toFile());
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					// We only care about native files
					if (entry.getName().endsWith(".so") || entry.getName().endsWith(".dll")) {
						File destFile = new File(nativesDirectory.getAbsolutePath() + File.separator + entry.getName());
						if (!destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs()) {
							throw new Exception("Unable to create native directory for: " + entry.getName());
						}
						InputStream stream = jar.getInputStream(entry);
						FileOutputStream output = new FileOutputStream(destFile);
						while (stream.available() > 0) {
							output.write(stream.read());
						}
						stream.close();
						output.close();
						natives.put(entry.getName(), destFile);
						System.out.println("Extracted " + entry.getName());
					}
				}
				jar.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			System.err.println("Unable to locate self");
		}
	}

	public static File getNativesDirectory() throws Exception {
		File nativesDirectory;
		LocationUtil self = LocationUtil.getClassPhysicalLocation(Main.class);
		if (self.toFile() != null && self.toFile().exists()) {
			nativesDirectory = new File(self.toFile().getParentFile().getAbsoluteFile() + File.separator + "natives" + File.separator);
			if (!nativesDirectory.exists() && !nativesDirectory.mkdirs()) {
				throw new Exception("Unable to create natives directory");
			}
		} else {
			throw new Exception("Unable to find self!");
		}
		return nativesDirectory;
	}

	/**
	 * Loads the external lwjgl natives
	 */
	public static void loadNatives() throws Exception {
		for (File nativeLib : natives.values()) {
			if (nativeLib.getName().endsWith(".so") && OSUtils.isLinux() || nativeLib.getName().endsWith(".dll") && OSUtils.isWindows()) {
				System.out.println("Loading " + nativeLib.getName());
				System.load(nativeLib.getAbsolutePath());
			}
		}
		System.setProperty("org.lwjgl.librarypath", getNativesDirectory().getAbsolutePath());
		System.out.println("Loaded libraries");
	}

}
