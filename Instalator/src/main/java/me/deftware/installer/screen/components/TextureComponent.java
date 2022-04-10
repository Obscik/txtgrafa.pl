package me.deftware.installer.screen.components;

import me.deftware.installer.resources.ResourceUtils;
import me.deftware.installer.resources.Texture;
import me.deftware.installer.screen.AbstractComponent;

import java.util.function.Consumer;

/**
 * A texture which can be rendered on a screen
 *
 * @author Deftware
 */
public class TextureComponent extends AbstractComponent<TextureComponent> {

	private Texture texture;
	private final Consumer<Integer> clickCallback;

	public TextureComponent(float x, float y, String path) {
		this(x, y, path, 1, null);
	}

	public TextureComponent(float x, float y, String path, Consumer<Integer> clickCallback) {
		this(x, y, path, 1, clickCallback);
	}

	public TextureComponent(float x, float y, String path, int scale) {
		this(x, y, path, scale, null);
	}

	public TextureComponent(float x, float y, String path, int scale, Consumer<Integer> clickCallback) {
		super(x, y);
		try {
			texture = ResourceUtils.loadTexture(path, scale);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.clickCallback = clickCallback;
	}

	@Override
	public float getWidth() {
		return texture.getWidth();
	}

	@Override
	public float getHeight() {
		return texture.getHeight();
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		try {
			texture.draw(x, y);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void update() { }

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > this.getX() && x < this.getX() + texture.getWidth() && y > this.getY() && y < this.getY() + texture.getHeight() && clickCallback != null && mouseButton == 0) {
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
