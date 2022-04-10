package me.deftware.installer.resources.font;

import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * @author Deftware
 */
public class BitmapFont {

	protected int lastRenderedWidth;
	protected int lastRenderedHeight;

	protected String fontName;
	protected int fontSize, shadowSize = 1;
	protected boolean antialiased;
	protected boolean memorysaving;
	protected Font stdFont;

	protected HashMap<Character, Integer> textureIDStore = new HashMap<>();
	protected HashMap<Character, int[]> textureDimensionsStore = new HashMap<>();

	public BitmapFont(String fontName, int fontSize, int modifiers) {
		this.fontName = fontName;
		this.fontSize = fontSize;

		this.antialiased = ((modifiers & 32) != 0);
		this.memorysaving = ((modifiers & 64) != 0);

		prepareStandardFont();

		lastRenderedWidth = 0;
		lastRenderedHeight = 0;
	}

	protected void prepareStandardFont() {
		if (FontManager.customFonts.containsKey(fontName)) {
			this.stdFont = FontManager.customFonts.get(fontName).deriveFont(Font.PLAIN, fontSize);
		} else {
			this.stdFont = new Font(fontName, Font.PLAIN, fontSize);
		}
	}

	public int initialize(Color color, String extras) {
		if (extras == null)
			extras = "";
		char[] additionalCharacters = extras.toCharArray();
		//Lowercase alphabet
		for (char lowercaseAlphabet = 'a'; lowercaseAlphabet <= 'z'; lowercaseAlphabet++) {
			characterGenerate(lowercaseAlphabet, color);
		}
		//Uppercase alphabet
		for (char uppercaseAlphabet = 'A'; uppercaseAlphabet <= 'Z'; uppercaseAlphabet++) {
			characterGenerate(uppercaseAlphabet, color);
		}
		//Numbers
		for (char numeric = 48; numeric <= 57; numeric++) { //0 - 9 in ASCII
			characterGenerate(numeric, color);
		}
		char[] specialCharacters = {'!', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
				':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~', '"'};
		if (additionalCharacters.length > 0)
			specialCharacters = ArrayUtils.addAll(specialCharacters, additionalCharacters);
		//Additional and special characters
		for (char specialCharacter : specialCharacters) { //0 - 9 in ASCII
			characterGenerate(specialCharacter, color);
		}
		return 0;
	}

	protected void characterGenerate(char character, Color color) {
		String letterBuffer = String.valueOf(character);
		int textwidth = getStringWidth(letterBuffer), textheight = getStringHeight(letterBuffer);

		BufferedImage characterTexture = new BufferedImage(textwidth, textheight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = characterTexture.createGraphics();
		graphics.setFont(stdFont);
		graphics.setColor(color);
		if (antialiased) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		}
		graphics.drawString(letterBuffer, 0, textheight - textheight / 4);
		graphics.dispose();

		textureIDStore.put(character, GraphicsUtil.loadTextureFromBufferedImage(characterTexture));
		textureDimensionsStore.put(character, new int[]{characterTexture.getWidth(), characterTexture.getHeight()});
	}

	public int drawString(int x, int y, String text) {
		return drawString(x, y, text, ThemeEngine.getTheme().getTextColor(), ThemeEngine.getTheme().isTextShadow());
	}

	public int drawStringWithShadow(int x, int y, String text) {
		return drawString(x, y, text, ThemeEngine.getTheme().getTextColor(), true);
	}

	public int drawString(int x, int y, String text, Color color, boolean shadow) {
		if (shadow) {
			drawString(x + shadowSize, y + shadowSize, text, Color.black);
		}
		drawString(x, y, text, color);
		return 0;
	}

	public int drawStringWithShadow(int x, int y, String text, Color color) {
		return drawString(x, y, text, color, true);
	}

	public int drawString(int x, int y, String text, Color color) {
		char[] buffer = text.toCharArray();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int offset = 0;
		for (int character = 0; character < buffer.length; character++) {

			if (buffer[character] == ' ') {
				offset += getStringWidth(" ");
				continue;
			} else if (!textureIDStore.containsKey(buffer[character])) {
				buffer[character] = '?';
			}
			if (color != null) {
				float red = color.getRed() > 0 ? color.getRed() * (1f / 255f) : 0;
				float green = color.getGreen() > 0 ? color.getGreen() * (1f / 255f) : 0;
				float blue = color.getBlue() > 0 ? color.getBlue() * (1f / 255f) : 0;
				float alpha = color.getAlpha() > 0 ? color.getAlpha() * (1f / 255f) : 0;
				GL11.glColor4f(red, green, blue, alpha);
			}
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDStore.get(buffer[character]));
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			int width = textureDimensionsStore.get(buffer[character])[0];
			int height = textureDimensionsStore.get(buffer[character])[1];
			GraphicsUtil.drawQuads(x + offset, y, width, height); //GL PART //8745

			offset += width;
		}
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		lastRenderedWidth = offset;
		RenderSystem.glColor(Color.white);
		return 0;
	}

	public int getStringWidth(String text) {
		FontMetrics fontMetrics = new Canvas().getFontMetrics(stdFont);
		return text != null && fontMetrics != null ? fontMetrics.charsWidth(text.toCharArray(), 0, text.length()) : 0;
	}

	public int getStringHeight(String text) {
		FontMetrics fontMetrics = new Canvas().getFontMetrics(stdFont);
		return fontMetrics.getHeight();
	}

	public int getLastRenderedHeight() {
		return lastRenderedHeight;
	}

	public int getLastRenderedWidth() {
		return lastRenderedWidth;
	}

	public void destroy() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); //Bind texture to 0 - unbind everything
		for (Character key : textureIDStore.keySet()) {
			GL11.glDeleteTextures(textureIDStore.get(key));
		}
		textureIDStore.clear();
		textureDimensionsStore.clear();
	}

	public String getFontName() {
		return fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		prepareStandardFont();
	}

	public int getShadowSize() {
		return shadowSize;
	}

	public void setShadowSize(int shadowSize) {
		this.shadowSize = shadowSize;
	}

	public boolean isAntialiased() {
		return antialiased;
	}

	public void setAntialiased(boolean antialiased) {
		this.antialiased = antialiased;
	}

	public boolean isMemorysaving() {
		return memorysaving;
	}

	public void setMemorysaving(boolean memorysaving) {
		this.memorysaving = memorysaving;
	}

}
