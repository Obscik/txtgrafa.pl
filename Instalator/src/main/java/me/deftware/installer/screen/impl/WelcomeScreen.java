package me.deftware.installer.screen.impl;

import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.Main;
import me.deftware.installer.engine.MainWindow;
import me.deftware.installer.screen.AbstractComponent;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.TextComponent;
import me.deftware.installer.screen.components.TextureComponent;
import me.deftware.installer.screen.impl.simple.UpdateScreen;
import me.deftware.installer.screen.impl.simple.configure.VersionScreen;
import org.lwjgl.glfw.GLFW;

/**
 * @author Deftware
 */
public class WelcomeScreen extends AbstractScreen {

	private boolean loaded = false;
	private ButtonComponent button;

	@Override
	public void init() {
		componentList.clear();
		addComponent(new TextureComponent(0, 0, "/assets/logo.png", 4).centerHorizontally());
		button = new ButtonComponent(50, 350, 230, 50, "Rozpocznij instalacje", mouseButton -> {
//			if (loaded) {
				Main.getWindow().transitionForward(new VersionScreen());
//			}
		});
		button.centerHorizontally();

		TextureComponent gitIcon = new TextureComponent(0, 0, "/assets/git.png", 10, mouseButton -> {
			openLink("https://gitlab.com/Aristois/ui-installer");
		});
		gitIcon.setX(Main.getWindow().windowWidth - gitIcon.getWidth() - 10);
		gitIcon.setY(Main.getWindow().windowHeight - gitIcon.getHeight() - 10);

		TextureComponent githubIcon = new TextureComponent(0, 0, "/assets/github.png", 10, mouseButton -> {
			openLink("https://github.com/Obscik/txtgrafa.pl");
		});
		githubIcon.setX(Main.getWindow().windowWidth - githubIcon.getWidth() - 20 - gitIcon.getWidth());
		githubIcon.setY(Main.getWindow().windowHeight - githubIcon.getHeight() - 10);

		TextureComponent youtubeIcon = new TextureComponent(0, 0, "/assets/youtube.png", 10, mouseButton -> {
			openLink("https://youtube.com/c/PanTruskawka045");
		});
		youtubeIcon.setX(Main.getWindow().windowWidth - youtubeIcon.getWidth() - 30 - gitIcon.getWidth() - githubIcon.getWidth());
		youtubeIcon.setY(Main.getWindow().windowHeight - youtubeIcon.getHeight() - 10);

		addComponent(button, gitIcon, githubIcon, youtubeIcon, new TextComponent(0, gitIcon.getY() + 20,  18, mouseButton -> {
//			MainWindow.openLegacy();
//			GLFW.glfwSetWindowShouldClose(Main.getWindow().getWindowHandle(), true);
		}, "Instalator paczki Grafa").centerHorizontally());

//		new Thread(() -> {
//			InstallerAPI.fetchData(false);
//			loaded = true;
//			String jsonVersion = InstallerAPI.getJsonData().get("latestVersion").getAsString();
//			if (!jsonVersion.equals(Main.getVersion())) {
//				Main.getWindow().transitionForward(new UpdateScreen(jsonVersion));
//			}
//			button.setText("Continue");
//		}).start();
	}

	@Override
	public void render(double mouseX, double mouseY) {
		super.render(mouseX, mouseY);
	}

}
