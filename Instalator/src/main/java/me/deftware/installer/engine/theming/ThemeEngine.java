package me.deftware.installer.engine.theming;

import lombok.Getter;
import me.deftware.installer.Main;

import java.awt.*;

/**
 * Manages theming throughout the various components
 *
 * @author Deftware
 */
public class ThemeEngine {

	/**
	 * The current theme used by the window and components
	 */
	private @Getter static ITheme theme = DefaultThemes.BLUE;

	/**
	 * Returns a specified color with a specified alpha between 1 and 255 applied
	 */
	public static Color getColorWithAlpha(Color color, float alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) alpha);
	}

	/**
	 * Blends two or more colors together based on a given ratio between 0.1 and 1
	 */
	public static Color blend(float ratio, Color... colors) {
		int r = 0, g = 0, b = 0, a = 0;
		for (Color color : colors) {
			r += color.getRed() * ratio;
			g += color.getGreen() * ratio;
			b += color.getBlue() * ratio;
			a += color.getAlpha() * ratio;
		}
		return new Color(r, g, b, a);
	}

	/**
	 * Sets the current theme
	 */
	public static void setTheme(ITheme theme) {
		ITheme oldTheme = ThemeEngine.theme;
		ThemeEngine.theme = theme;
		if (!oldTheme.getTextFont().equalsIgnoreCase(theme.getTextFont())) {
			Main.getWindow().setScheduleRefresh(true);
		}
	}

}
