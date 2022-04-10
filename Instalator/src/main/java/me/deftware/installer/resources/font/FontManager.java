package me.deftware.installer.resources.font;

import me.deftware.installer.Main;

import java.awt.*;
import java.util.HashMap;

/**
 * Manages fonts used by various components
 *
 * @author Deftware
 */
public class FontManager {

	private static HashMap<String, BitmapFont> fontStore = new HashMap<>();
	public static HashMap<String, Font> customFonts = new HashMap<>();

	/**
	 * This is the main way to get a custom font, it will cache it after getting it the first time for
	 * optimal performance. If a given font cannot be found, it defaults to Arial
	 */
	public static BitmapFont getFont(String name, int size, int modifiers) {
		String key = name + size + modifiers;
		if (fontStore.containsKey(key)) {
			return fontStore.get(key);
		}
		fontStore.put(key, new BitmapFont(name, size, modifiers));
		return fontStore.get(key);
	}

	/**
	 * Adds a custom font to the available fonts the BitMap font can use
	 */
	public static void registerCustomFont(Font font) {
		customFonts.putIfAbsent(font.getFontName(), font);
	}

	/**
	 * Removes a certain font from the cache
	 */
	public static void removeFont(String name, int size, int modifiers) {
		fontStore.remove(name + size + modifiers);
	}

	/**
	 * Clears the font cache
	 */
	public static void clearCache() {
		fontStore.clear();
		customFonts.clear();
	}

	/**
	 * Loads a custom .ttf font from the /assets/ dir of the jar itself
	 * Must be called prior to using a custom font with a custom theme
	 */
	public static void loadFontFromAssets(String path) throws Exception {
		Font customFont = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream(path));
		registerCustomFont(customFont);
	}

	/**
	 * Various modifiers that can be used with fonts,
	 * ANTIALIASED is always recommended to be enabled
	 */
	public static class Modifiers {
		// public static byte NONE = 0b00000000;
		public static byte ANTIALIASED = 0b00100000;
		// public static byte MEMORYSAVING = 0b01000000;
	}

}
