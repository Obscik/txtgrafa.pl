package me.deftware.installer.screen.impl.simple.configure;

import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.aristois.installer.utils.VersionData;
import me.deftware.installer.Main;
import me.deftware.installer.OSUtils;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;
import me.deftware.installer.screen.impl.simple.InstallingScreen;
import me.deftware.installer.screen.impl.simple.TransitionScreen;
import me.deftware.installer.screen.impl.simple.YesNoScreen;

import java.io.File;

/**
 * @author Deftware
 */
public class LauncherScreen extends AbstractScreen {

	private String version;

	public LauncherScreen(String version) {
		this.version = version;
	}

	@Override
	public void init() {
		componentList.clear();

		TextBoxComponent minecraftPath = new BrowsableTextBoxComponent(0, 300, 700, 30, OSUtils.getMCDir() + "resourcepacks");
		minecraftPath.setShadowText("Sciezka");
		minecraftPath.centerHorizontally();

		addComponent(new TextComponent(0, 65,  40, "Sciezka").centerHorizontally(),
				new TextComponent(0, 130,  25, "Wybierz sciezke, w ktorej zostanie zainstalowany texturepack.", "Potem kliknij \"Kontynuuj\".").centerHorizontally(),
				new ButtonComponent(50, 400, 100, 50, "Kontynuuj", mouseButton -> {
					Main.getWindow().transitionForward(new InstallingScreen(version, minecraftPath.getText()));
				}).centerHorizontally(), minecraftPath);
	}

}
