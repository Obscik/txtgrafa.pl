package me.deftware.installer.screen.components.effects;

import me.deftware.installer.engine.theming.ThemeEngine;

import java.awt.*;

/**
 * @author Deftware
 */
public class BlendableEffect {

	private float ratio = 0.5f;
	private Color currentColor = ThemeEngine.getTheme().getForegroundColor();

	public Color getCurrentColor(float alpha) {
		return new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), (int) alpha);
	}

	public void update(boolean mouseOver) {
		if (mouseOver) {
			if (ratio > 0.4f) {
				ratio -= 0.02f;
			}
		} else {
			if (ratio < 0.5f) {
				ratio += 0.02f;
			}
		}
		currentColor = ThemeEngine.blend(ratio, ThemeEngine.getTheme().getBackgroundColor().brighter().brighter(), ThemeEngine.getTheme().getForegroundColor());
	}

}
