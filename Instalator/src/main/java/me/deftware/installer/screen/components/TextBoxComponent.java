package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A textbox
 *
 * @author Deftware
 */
public class TextBoxComponent extends AbstractComponent<TextBoxComponent> {

	protected @Getter String text;
	protected @Getter float width, height;
	protected @Setter String shadowText = "";
	protected @Getter TextComponent textComponent;
	protected @Getter @Setter int maxTextLength = 300;
	protected @Getter @Setter boolean readOnly = false;
	protected @Setter Consumer<String> onChangedCallback;

	protected @Setter float textAllowedWidth;
	protected long lastMs = System.currentTimeMillis();
	protected boolean cursorTick = false, selecting = false;
	protected int firstCharacterIndex, selectionStart, selectionEnd;

	public TextBoxComponent(float x, float y, float width, int fontSize, String text) {
		super(x, y);
		this.width = width;
		textComponent = new TextComponent(x, y, fontSize, text);
		textComponent.setCenteredText(false);
		this.height = textComponent.getHeight();
		this.text = text;
		this.textAllowedWidth = width;
	}

	public void setText(String text) {
		if (text.length() > this.maxTextLength) {
			this.text = text.substring(0, this.maxTextLength);
		} else {
			this.text = text;
		}
		this.setCursor(this.text.length());
		this.setSelectionEnd(this.selectionStart);
		this.onChanged(text);
	}

	private void appendText(String string) {
		int minSelection = Math.min(this.selectionStart, this.selectionEnd), maxSelection = Math.max(this.selectionStart, this.selectionEnd);
		int deltaLength = this.maxTextLength - this.text.length() - (minSelection - maxSelection), length = string.length();
		if (deltaLength < length) {
			string = string.substring(0, deltaLength);
			length = deltaLength;
		}
		this.text = (new StringBuilder(this.text)).replace(minSelection, maxSelection, string).toString();
		this.setSelectionStart(minSelection + length);
		this.setSelectionEnd(this.selectionStart);
		this.onChanged(this.text);
	}

	public String getSelectedText() {
		return this.text.substring(Math.min(this.selectionStart, this.selectionEnd),
				Math.max(this.selectionStart, this.selectionEnd));
	}

	private void onChanged(String newText) {
		if (this.onChangedCallback != null) {
			this.onChangedCallback.accept(newText);
		}
	}

	private void backspaceWords(int wordOffset) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.appendText("");
			} else {
				this.backspaceCharacters(this.getWordSkipPosition(wordOffset) - this.selectionStart);
			}
		}
	}

	private void backspaceCharacters(int characterOffset) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.selectionStart) {
				this.appendText("");
			} else {
				int cursorPosition = moveCursor(text, selectionStart, characterOffset);
				int minPosition = Math.min(cursorPosition, this.selectionStart);
				int maxPosition = Math.max(cursorPosition, this.selectionStart);
				if (minPosition != maxPosition) {
					this.text = (new StringBuilder(this.text)).delete(minPosition, maxPosition).toString();
					this.setCursor(minPosition);
				}
			}
		}
	}

	private int getWordSkipPosition(int wordOffset) {
		int cursorPosition = selectionStart;
		for(int i = 0; i < Math.abs(wordOffset); ++i) {
			if (wordOffset > 0) {
				int length = this.text.length();
				cursorPosition = this.text.indexOf(maxTextLength, cursorPosition);
				if (cursorPosition == -1) {
					cursorPosition = length;
				} else {
					while(cursorPosition < length && this.text.charAt(cursorPosition) == ' ') {
						++cursorPosition;
					}
				}
			} else {
				while(cursorPosition > 0 && this.text.charAt(cursorPosition - 1) == ' ') {
					--cursorPosition;
				}
				while(cursorPosition > 0 && this.text.charAt(cursorPosition - 1) != ' ') {
					--cursorPosition;
				}
			}
		}
		return cursorPosition;
	}

	private void setSelectionEnd(int i) {
		int j = this.text.length();
		this.selectionEnd = clamp(i, 0, j);
		if (this.firstCharacterIndex > j) {
			this.firstCharacterIndex = j;
		}

		String string = this.textComponent.trimToWidth(this.text.substring(this.firstCharacterIndex), textAllowedWidth);
		int l = string.length() + this.firstCharacterIndex;
		if (this.selectionEnd == this.firstCharacterIndex) {
			this.firstCharacterIndex -= this.textComponent.trimToWidthReverse(this.text, textAllowedWidth).length();
		}

		if (this.selectionEnd > l) {
			this.firstCharacterIndex += this.selectionEnd - l;
		} else if (this.selectionEnd <= this.firstCharacterIndex) {
			this.firstCharacterIndex -= this.firstCharacterIndex - this.selectionEnd;
		}

		this.firstCharacterIndex = clamp(this.firstCharacterIndex, 0, j);
	}

	private int moveCursor(int delta) {
		return moveCursor(text, selectionStart, delta);
	}

	private int moveCursor(String string, int cursor, int delta) {
		int i = string.length();
		int j;
		if (delta >= 0) {
			for(j = 0; cursor < i && j < delta; ++j) {
				if (Character.isHighSurrogate(string.charAt(cursor++)) && cursor < i && Character.isLowSurrogate(string.charAt(cursor))) {
					++cursor;
				}
			}
		} else {
			for(j = delta; cursor > 0 && j < 0; ++j) {
				--cursor;
				if (Character.isLowSurrogate(string.charAt(cursor)) && cursor > 0 && Character.isHighSurrogate(string.charAt(cursor - 1))) {
					--cursor;
				}
			}
		}

		return cursor;
	}

	private static int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		} else {
			return Math.min(value, max);
		}
	}

	private void setCursor(int cursor) {
		this.setSelectionStart(cursor);
		if (!this.selecting) {
			this.setSelectionEnd(this.selectionStart);
		}
		this.onChanged(this.text);
	}

	private void setSelectionStart(int cursor) {
		this.selectionStart = clamp(cursor, 0, this.text.length());
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		// Border
		RenderSystem.drawRect(x, y, x + width, y + height, ThemeEngine.getTheme().getOutlineColor());
		RenderSystem.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, ThemeEngine.getTheme().getBackgroundColor());

		int deltaStart = this.selectionStart - this.firstCharacterIndex, deltaEnd = this.selectionEnd - this.firstCharacterIndex;
		String drawString = textComponent.trimToWidth(this.text.substring(this.firstCharacterIndex), textAllowedWidth);
		boolean lengthOutsideBounds = deltaStart >= 0 && deltaStart <= drawString.length();

		if (deltaEnd > drawString.length()) {
			deltaEnd = drawString.length();
		}

		float xPos = x;
		if (!drawString.isEmpty()) {
			xPos = textComponent.drawString(x + 3, y, ThemeEngine.getTheme().getTextColor(), lengthOutsideBounds ? drawString.substring(0, deltaStart) : drawString);
		}
		if (!drawString.isEmpty() && lengthOutsideBounds && deltaStart < drawString.length()) {
			this.textComponent.drawString(xPos, y, ThemeEngine.getTheme().getTextColor(), drawString.substring(deltaStart));
		}
		if (!lengthOutsideBounds) {
			xPos = deltaStart > 0 ? x + this.textAllowedWidth : x;
		}

		// Shadow text
		if (text.isEmpty() && !shadowText.isEmpty()) {
			textComponent.drawString(x + 3, y, ThemeEngine.getTheme().getTextColor().darker(), shadowText);
		}

		// Caret
		if (cursorTick && focused) {
			if (this.selectionStart < this.text.length() || this.text.length() >= this.getMaxTextLength()) {
				RenderSystem.drawRect(xPos, y, xPos + 1, y + height, ThemeEngine.getTheme().getTextColor());
			} else {
				textComponent.drawString(xPos + (text.isEmpty() && !shadowText.isEmpty() ? 3 : 0), y, ThemeEngine.getTheme().getTextColor(), "_");
			}
		}

		// Selection highlight
		if (deltaEnd != deltaStart) {
			RenderSystem.drawRect(xPos, y, x + textComponent.getStringWidth(drawString.substring(0, deltaEnd)), y + height, ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextHighlightColor(), 100));
		}
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (readOnly) return false;
		// Focus state
		boolean prevState = focused;
		focused = x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
		if (focused && mouseButton == 2) {
			this.appendText(Objects.requireNonNull(GLFW.glfwGetClipboardString(Main.getWindow().windowHandle)));
		} else {
			focused &= mouseButton == 0;
		}
		// Text selection
		if (focused) {
			String string = textComponent.trimToWidth(this.text.substring(this.firstCharacterIndex), textAllowedWidth);
			this.setCursor(textComponent.trimToWidth(string, (float) Math.floor(x) - this.x).length() + this.firstCharacterIndex);
		} else {
			selecting = false;
		}
		if (!prevState && focused) {
			lastMs = System.currentTimeMillis();
			cursorTick = true;
			return true;
		}
		return false;
	}

	@Override
	public void charTyped(char typedChar) {
		if (!readOnly && focused) {
			this.appendText(Character.toString(typedChar));
		}
	}

	@Override
	public int cursorTest(double x, double y) {
		if (x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight() && !readOnly) {
			return GLFW.GLFW_IBEAM_CURSOR;
		}
		return GLFW.GLFW_ARROW_CURSOR;
	}

	@Override
	public void deFocus() {
		focused = false;
	}

	@Override
	public void update() {
		super.update();
		if (focused && lastMs + 500 < System.currentTimeMillis()) {
			lastMs = System.currentTimeMillis();
			cursorTick = !cursorTick;
		}
	}

	@Override
	public void keyPressed(int keycode, int mods) {
		if (readOnly || !focused) return;
		boolean selecting = mods == GLFW.GLFW_MOD_SHIFT;
		boolean ctrlKey = mods == GLFW.GLFW_MOD_CONTROL;
		this.selecting = selecting;
		if (ctrlKey && keycode == GLFW.GLFW_KEY_A) {
			this.setCursor(this.text.length());
			this.setSelectionEnd(0);
		} else if (ctrlKey && keycode == GLFW.GLFW_KEY_C) {
			GLFW.glfwSetClipboardString(Main.getWindow().windowHandle, getSelectedText());
		} else if (ctrlKey && keycode == GLFW.GLFW_KEY_V) {
			this.appendText(Objects.requireNonNull(GLFW.glfwGetClipboardString(Main.getWindow().windowHandle)));
		} else if (ctrlKey && keycode == GLFW.GLFW_KEY_X) {
			GLFW.glfwSetClipboardString(Main.getWindow().windowHandle, getSelectedText());
			this.appendText("");
		} else {
			switch (keycode) {
				case GLFW.GLFW_KEY_BACKSPACE:
					this.selecting = false;
					if (ctrlKey) {
						this.backspaceWords(-1);
					} else {
						this.backspaceCharacters(-1);
					}
					this.selecting = selecting;
					break;
				case GLFW.GLFW_KEY_RIGHT:
				case GLFW.GLFW_KEY_LEFT:
					if (ctrlKey) {
						this.setCursor(this.getWordSkipPosition(keycode == GLFW.GLFW_KEY_LEFT ? -1 : 1));
					} else {
						this.setCursor(this.moveCursor(keycode == GLFW.GLFW_KEY_LEFT ? -1 : 1));
					}
					break;
				case GLFW.GLFW_KEY_HOME:
					this.setCursor(0);
					break;
				case GLFW.GLFW_KEY_END:
					this.setCursor(this.text.length());
					break;
			}
		}
	}


	@Override
	public void onScroll(double xPos, double yPos) { }

	@Override
	public void mouseReleased(double x, double y, int mouseButton) { }

}
