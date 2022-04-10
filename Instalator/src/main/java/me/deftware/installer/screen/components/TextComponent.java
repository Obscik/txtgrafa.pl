package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.*;
import java.util.function.Consumer;

/**
 * A label which can display multiline text
 *
 * @author Deftware
 */
public class TextComponent extends AbstractComponent<TextComponent> {

	private BitmapFont font;
	private @Setter Consumer<Integer> clickCallback;
	private @Getter String[] text;
	private @Getter @Setter boolean centeredText = true;
	private boolean customFont = false;
	private final int fontSize;

	public TextComponent(float x, float y, int size, String... text) {
		this(x, y, size, null, text);
	}

	public TextComponent(float x, float y, int size, Consumer<Integer> clickCallback, String... text) {
		super(x, y);
		this.text = text;
		this.clickCallback = clickCallback;
		this.fontSize = size;
		setupFont();
	}

	private void setupFont() {
		try {
			this.font = FontManager.getFont(ThemeEngine.getTheme().getTextFont(), fontSize, FontManager.Modifiers.ANTIALIASED);
			this.font.setShadowSize(1);
			this.font.initialize(Color.white, "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setText(String... text) {
		this.text = text;
	}

	public void setCustomFont(String fontName) {
		customFont = true;
		setupFont();
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public float getWidth() {
		int maxLength = 0;
		for (String line : text) {
			if (maxLength < font.getStringWidth(line)) {
				maxLength = font.getStringWidth(line);
			}
		}
		return maxLength;
	}

	@Override
	public float getHeight() {
		return font.getStringHeight("ABC123") * text.length;
	}

	public String trimToWidthReverse(String text, float maxWidth) {
		int offsetIndex = 0;
		for (int i = text.length(); i > -1; i--) {
			if (getStringWidth(text.substring(i)) > maxWidth) {
				offsetIndex = i;
				break;
			}
		}
		return text.substring(offsetIndex);
	}

	public String trimToWidth(String text, float maxWidth) {
		int numChars = 0;
		for (int i = 0; i < text.length(); i++) {
			if (getStringWidth(text.substring(0, i)) > maxWidth) {
				break;
			}
			numChars++;
		}
		return text.substring(0, numChars);
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		drawString(x, y, ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), text);
		if (!font.getFontName().equalsIgnoreCase(ThemeEngine.getTheme().getTextFont()) && !customFont) {
			setupFont();
		}
	}

	public float drawString(float x, float y, Color color, String... text) {
		for (String line : text) {
			if (!centeredText) {
				font.drawString((int) x, (int) y, line,  color);
			} else {
				font.drawString((int) (x + ((getWidth() / 2) - (font.getStringWidth(line) / 2))), (int) y, line, color);
			}
			y += font.getStringHeight(line);
		}
		return x + font.getStringWidth(text[0]);
	}

	public int getStringWidth(String s) {
		return font.getStringWidth(s);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > this.getX() && x < this.getX() + getWidth() && y > this.getY() && y < this.getY() + getHeight() && clickCallback != null && mouseButton == 0) {
			clickCallback.accept(mouseButton);
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(double x, double y, int mouseButton) { }

	@Override
	public void charTyped(char typedChar) { }

	@Override
	public void keyPressed(int keycode, int mods) { }

	@Override
	public void onScroll(double xPos, double yPos) { }

}
