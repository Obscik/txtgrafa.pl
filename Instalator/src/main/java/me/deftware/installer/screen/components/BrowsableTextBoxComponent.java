package me.deftware.installer.screen.components;

import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.screen.components.effects.BlendableEffect;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

/**
 * @author Deftware
 */
public class BrowsableTextBoxComponent extends TextBoxComponent {

	private final BlendableEffect blendableEffect = new BlendableEffect();
	private boolean mouseOver = false;

	public BrowsableTextBoxComponent(float x, float y, float width, int fontSize, String text) {
		super(x, y, width, fontSize, text);
		setReadOnly(true);
		textAllowedWidth -= height + 20;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		super.render(x, y, mouseX, mouseY);
		RenderSystem.drawRect(x + width - height + 1, y + 1, x + width - 1, y + height - 1, blendableEffect.getCurrentColor(alpha));
		textComponent.drawString((int) (x + width - height + 9), (int) (y), ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha),"...");
		mouseOver = mouseX > x + width - height && mouseX < x+ width && mouseY > y && mouseY < y + height;
	}

	@Override
	public int cursorTest(double x, double y) {
		if (x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight()) {
			if (x > getX() && x < getX() + getWidth() - height && y > getY() && y < getY() + getHeight()) {
				if (!readOnly) {
					return GLFW.GLFW_IBEAM_CURSOR;
				}
			} else {
				return GLFW.GLFW_HAND_CURSOR;
			}
		}
		return GLFW.GLFW_ARROW_CURSOR;
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() + width - height && x < getX() + width && y > getY() && y < getY() + height && mouseButton == 0) {
			String folder = TinyFileDialogs.tinyfd_selectFolderDialog("Select path", "");
			if (folder != null && !folder.isEmpty()) {
				text = folder;
			}
			return true;
		}
		return super.mouseClicked(x, y, mouseButton);
	}

	@Override
	public void update() {
		super.update();
		blendableEffect.update(mouseOver);
	}

}
