package me.deftware.installer.screen.impl.simple;

import me.deftware.aristois.installer.jsonbuilder.JsonBuilder;
import me.deftware.aristois.installer.modloader.impl.ForgeInstaller;
import me.deftware.aristois.installer.utils.VersionData;
import me.deftware.installer.Main;
import me.deftware.installer.MinecraftLauncher;
import me.deftware.installer.OSUtils;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.TextComponent;
import me.deftware.installer.screen.impl.TexturepackVersion;
import me.deftware.installer.screen.impl.WelcomeScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * @author Deftware
 */
public class InstallingScreen extends AbstractScreen {

	private String version;
	private String path;
	private TextComponent textComponent, subText;
	private ButtonComponent button;
	private int count = 0;
	private boolean stopped = false;

	public InstallingScreen(String version, String path) {
		this.version = version;
		this.path = path;
	}

	@Override
	public void init() {

		componentList.clear();
		textComponent = new TextComponent(0, 0,  40, "Instaluje paczke!");
		textComponent.centerHorizontally().centerVertically(-20);
		subText = new TextComponent(0, textComponent.getY() + textComponent.getHeight() + 10,  30, "Paczka Grafa za chwile zostanie zainstalowana");
		subText.centerHorizontally();

		button = new ButtonComponent(0, Main.getWindow().windowHeight - 110, 220, 50, "Instaluj inne wersje", mouseButton -> {
			Main.getWindow().transitionForward(new WelcomeScreen());
		});
		button.setAlpha(1);
		button.setVisible(false);

		addComponent(textComponent, subText, button.centerHorizontally());

		new Thread(() -> {
			String result = "";
			try {
				TexturepackVersion texturepackVersion = Main.getTexturepackVersions().get(this.version);
				System.out.println(texturepackVersion.getFilename());
				downloadFile(new URL(texturepackVersion.getUrl()), new File(path), texturepackVersion.getFilename());
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(result);
			subText.fadeOut(alpha -> fadeIn());
			textComponent.fadeOut(alpha -> fadeIn());
		}, "Installer thread").start();
	}

	private void fadeIn() {
		count++;
		if (count == 2) {
			textComponent.setText("Poprawnie zainstalowano!");
			subText.setText("Mozesz teraz uzywac paczki zasobow", "\"" + version + "\"");

			//			if (launcher.toLowerCase().contains("vanilla")) {
//			} else if (launcher.toLowerCase().contains("forge")) {
//				subText.setText("Start Minecraft and select", "Forge " + version.getVersion() + " to start", "Aristois");
//			}
			textComponent.centerHorizontally().centerVertically(-60);
			subText.center();
			subText.setY(textComponent.getY() + textComponent.getHeight() + 10);
			textComponent.fadeIn(aplha -> {
				button.setVisible(true);
				button.fadeIn(alpha -> {});
			});
			subText.fadeIn(null);
		}
	}

	public static void downloadFile(URL url, File root, String outputFileName) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
		FileOutputStream fos = new FileOutputStream(new File(root, outputFileName));
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}

}
