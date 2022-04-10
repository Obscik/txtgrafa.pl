package me.deftware.installer;

import lombok.Getter;
import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.engine.NativeManager;
import me.deftware.installer.engine.MainWindow;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.impl.TexturepackVersion;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Deftware
 */
public class Main {

	@Getter
	private static final Map<String, TexturepackVersion> texturepackVersions = new LinkedHashMap<>();

	static {
		texturepackVersions.put("Overlay 1.18", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.18%20%C2%A7fGrudzien%202021.zip", "\u00A7bOverlay Grafa \u00A7a1.18 \u00A7fGrudzien 2021.zip"));
		texturepackVersions.put("Ramki do rud 1.18", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%C2%A7a1.18.zip", "\u00A7bRamki do rud \u00A7a1.18.zip"));
		texturepackVersions.put("Overlay 1.17", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.17%20%C2%A7fListopad%202021.zip", "\u00A7bOverlay Grafa \u00A7a1.17 \u00A7fListopad 2021.zip"));
		texturepackVersions.put("Ramki do rud 1.17", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%C2%A7a1.17.zip", "\u00A7bRamki do rud \u00A7a1.17.zip"));
		texturepackVersions.put("Overlay 1.16", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.16%20%C2%A7fMarzec%202021.zip", "\u00A7bOverlay Grafa \u00A7a1.16 \u00A7fMarzec 2021.zip"));
		texturepackVersions.put("Ramki do rud 1.16", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%C2%A7a1.16.zip", "\u00A7bRamki do rud \u00A7a1.16.zip"));
		texturepackVersions.put("Overlay 1.15", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.15%C2%A7fSierpie%C5%84%202020.zip", "\u00A7bOverlay Grafa \u00A7a1.15\u00A7fSierpień 2020.zip"));
		texturepackVersions.put("Ramki do rud 1.15", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%C2%A7a1.15.zip", "\u00A7bRamki do rud \u00A7a1.15.zip"));
		texturepackVersions.put("Overlay 1.14", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.14%20%C2%A7fSierpie%C5%84%202020.zip", "\u00A7bOverlay Grafa \u00A7a1.14 \u00A7fSierpień 2020.zip"));
		texturepackVersions.put("Ramki do rud 1.14", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%20%C2%A7a1.14.zip", "\u00A7bRamki do rud  \u00A7a1.14.zip"));
		texturepackVersions.put("Overlay 1.12", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.12%20%C2%A7fSierpie%C5%84%202020.zip", "\u00A7bOverlay Grafa \u00A7a1.12 \u00A7fSierpień 2020.zip"));
		texturepackVersions.put("Ramki do rud 1.12", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%C2%A7a1.12.zip", "\u00A7bRamki do rud \u00A7a1.12.zip"));
		texturepackVersions.put("Overlay 1.8", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bOverlay%20Grafa%20%C2%A7a1.8%20%C2%A7fSierpie%C5%84%202020.zip", "\u00A7bOverlay Grafa \u00A7a1.8 \u00A7fSierpień 2020.zip"));
		texturepackVersions.put("Ramki do rud 1.8", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bRamki%20do%20rud%20%C2%A7a1.8.zip", "\u00A7bRamki do rud \u00A7a1.8.zip"));
		texturepackVersions.put("Stary wyglad efficiency V", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7bLegacy%20Efficiency%20%C2%A7a1.15.zip", "\u00A7bLegacy Efficiency \u00A7a1.15.zip"));
		texturepackVersions.put("Disco Ziemniaki", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7eDisco%20Ziemniaki.zip", "\u00A7eDisco Ziemniaki.zip"));
		texturepackVersions.put("Niska Tarcza", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7eNiska%20Tarcza.zip", "\u00A7eNiska Tarcza.zip"));
		texturepackVersions.put("Rozowe ramki 1.16", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7dR%C3%B3%C5%BCowe%20%C2%A7bRamki%20do%20rud.zip", "\u00A7dRóżowe \u00A7bRamki do rud.zip"));
		texturepackVersions.put("Rozowy Overlay 1.16", new TexturepackVersion("https://txtgrafa.pl/pliki/txt/%C2%A7dR%C3%B3%C5%BCowy%20%C2%A76Overlay%20Grafa%20%C2%A7a1.16%20%C2%A7fMarzec%202021.zip", "\u00A7dRóżowy \u00A76Overlay Grafa \u00A7a1.16 \u00A7fMarzec 2021.zip"));
	}


	private final static String donorString = "@DONOR@";

	@Getter
	public static String version = "@VERSION@";

	@Getter
	public static MainWindow window;

	@SuppressWarnings("ConstantConditions")
	public static void main(String[] args) {
		if (args.length != 0 && !OSUtils.isMac()) {
			System.setProperty("org.lwjgl.util.Debug", "true");
			try {
				NativeManager.loadNatives();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Running Java " + System.getProperty("java.version"));
		System.out.println("OS arch " + System.getProperty("os.arch"));
		System.out.println("Installer version " + version);
		System.out.println("Donor build " + donorString);
		InstallerAPI.setDonorBuild(Boolean.parseBoolean(donorString));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			FontManager.loadFontFromAssets("/assets/NotoSans-Regular.ttf");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// opengl is deprecated in macOS
		if (OSUtils.isMac()) {
			MainWindow.openLegacy();
		} else {
			try {
				window = new MainWindow();
				window.run();
			} catch (UnsatisfiedLinkError ex) {
				System.err.println("Unsatisfied lwjgl link error, trying to fix it...");
				if (args.length == 0) {
					NativeManager.extractNatives();
					System.out.println("Restarting app...");
					restart();
				} else {
					System.out.println("Failed to load native libraries, defaulting to legacy mode...");
					MainWindow.openLegacy();
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
				MainWindow.openLegacy();
			}
		}
	}

	public static void restart() {
		try {
			LocationUtil self = LocationUtil.getClassPhysicalLocation(Main.class);
			if (self.toFile() != null && self.toFile().exists()) {
				Runtime.getRuntime().exec("java -jar " + self.toFile().getAbsolutePath() + " --loadNatives");
				System.exit(0);
			} else {
				throw new Exception("Could not find self");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			MainWindow.openLegacy();
		}
	}

}
