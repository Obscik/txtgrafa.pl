package me.deftware.installer.screen;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

/**
 * @author Deftware
 */
public abstract class AbstractComponent<T> {

	protected @Getter @Setter float x, y;
	protected @Getter @Setter boolean visible = true;
	protected @Getter @Setter boolean focused = false;

	/**
	 * Tooltips can be split into multilines using the /n delimiter
	 */
	protected @Getter @Setter String tooltip = "";

	/**
	 * 0 = Not enabled
	 * 1 = Fade in
	 * 2 = Fade out
	 */
	protected @Getter @Setter int fadeStatus = 0, alpha = 255;
	protected double iterations = 50, i = 0, counter = 0, increase = Math.PI / iterations;
	protected Consumer<Integer> fadeCallback;

	public AbstractComponent(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public abstract float getWidth();

	public abstract float getHeight();

	public abstract void render(float x, float y, double mouseX, double mouseY);

	public void update() {
		if (fadeStatus != 0) {
			if (i <= 1 && (fadeStatus == 2 && alpha > 3 || fadeStatus == 1 && alpha < 255)) {
				double current = Math.sin(counter) * (255 / iterations * counter);
				alpha = (int) (fadeStatus == 2 ? alpha - current : alpha + current);
				counter += increase;
				i += 1 / iterations;
			} else {
				fadeStatus = 0;
				i = 0;
				counter = 0;
				if (fadeCallback != null) {
					fadeCallback.accept(alpha);
				}
			}
		}
	}

	public abstract boolean mouseClicked(double x, double y, int mouseButton);

	public abstract void mouseReleased(double x, double y, int mouseButton);

	public abstract void charTyped(char typedChar);

	public abstract void keyPressed(int keycode, int mods);

	public abstract void onScroll(double xPos, double yPos);

	public int cursorTest(double mouseX, double mouseY) {
		return GLFW.GLFW_ARROW_CURSOR;
	}

	public void deFocus() {}

	public AbstractComponent<T> centerHorizontally(float offset) {
		x = ((AbstractScreen.getWindowWidth() / 2) - (getWidth() / 2)) + offset;
		return this;
	}

	public AbstractComponent<T> centerHorizontally() {
		return centerHorizontally(0);
	}

	public AbstractComponent<T> centerVertically(float offset) {
		y = (AbstractScreen.getWindowHeight() / 2) - (getHeight() / 2) + offset;
		return this;
	}

	public AbstractComponent<T> centerVertically() {
		return centerVertically(0);
	}

	public AbstractComponent<T> center() {
		return centerHorizontally().centerVertically();
	}

	public void fadeOut(Consumer<Integer> callback) {
		fadeCallback = callback;
		fadeStatus = 2;
	}

	public void fadeIn(Consumer<Integer> callback) {
		fadeCallback = callback;
		fadeStatus = 1;
	}

}
