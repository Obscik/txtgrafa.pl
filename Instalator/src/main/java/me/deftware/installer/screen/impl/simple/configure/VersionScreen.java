package me.deftware.installer.screen.impl.simple.configure;

import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.installer.Main;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;
import me.deftware.installer.screen.impl.simple.TransitionScreen;

import java.util.Arrays;

/**
 * @author Deftware
 */
public class VersionScreen extends AbstractScreen {

	@Override
	public void init() {
		componentList.clear();

		String[] versions = Main.getTexturepackVersions().keySet().toArray(new String[0]);
		ComboBoxComponent versionsBox = new ComboBoxComponent(0, 200, 600, 30, versions);
		versionsBox.centerHorizontally();

		addComponent(versionsBox, new TextComponent(0, 65,  40, "Ktora wersje zainstalowac?").centerHorizontally(),
				new TextComponent(0, 130,  25, "Wybierz wersje teksturepacka sposrod nizej wymienionych:").centerHorizontally(),
				new ButtonComponent(50, 400, 100, 50, "Kontynuuj", mouseButton -> {
					String version = versionsBox.getSelectedItem();
					Main.getWindow().transitionForward(new TransitionScreen("Wybrano wersje " + version, button -> {
						Main.getWindow().transitionForward(new LauncherScreen(version));
					}, 1700, "Teraz mozesz wybrac lokalizacje,", "w ktorej zostanie zainstalowana paczka zasobow."));
		}).centerHorizontally());
	}

}
