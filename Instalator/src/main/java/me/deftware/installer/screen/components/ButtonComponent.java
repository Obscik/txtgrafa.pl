package me.deftware.installer.screen.components;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.screen.AbstractComponent;
import me.deftware.installer.screen.components.effects.BlendableEffect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A clickable button
 *
 * @author Deftware
 */
public class ButtonComponent extends AbstractComponent<ButtonComponent> {

	private @Getter final TextComponent font;
	private @Getter final float width, height;
	private @Getter String text;
	private @Getter final List<Consumer<Integer>> onClickCallbacks = new ArrayList<>();
	private @Setter boolean visible = true;
	private boolean mouseOver = false;
	private final BlendableEffect blendableEffect = new BlendableEffect();

	public ButtonComponent(float x, float y, float width, float height, String text, Consumer<Integer> onClick) {
		super(x, y);
		// Account for circles
		this.width = width + (25 * 2);
		this.height = height;
		this.onClickCallbacks.add(onClick);
		this.text = text;
		font = new TextComponent(x, y, 23, text);
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		if (visible) {
			Color bgColor = blendableEffect.getCurrentColor(alpha);
			RenderSystem.drawRect(x + 25, y, x + width - 25, y + height, bgColor);
			RenderSystem.drawCircle(x + 25, y + 25, 25, bgColor);
			RenderSystem.drawCircle(x + width - 25, y + 25, 25, bgColor);
			font.drawString((int) (x + ((width / 2) - (font.getWidth()/ 2))), (int) (y + ((height / 2) - (font.getHeight() / 2))), ThemeEngine.getColorWithAlpha(ThemeEngine.getTheme().getTextColor(), alpha), text);
			mouseOver = mouseX > getX() && mouseX < getX() + width && mouseY > getY() && mouseY < getY() + height;
		}
	}

	@Override
	public void update() {
		super.update();
		blendableEffect.update(mouseOver);
	}

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
		if (x > getX() && x < getX() + width && y > getY() && y < getY() + height && visible && mouseButton == 0) {
			for (Consumer<Integer> cb : onClickCallbacks) {
				cb.accept(mouseButton);
			}
			return true;
		}
		return false;
	}

	public void setText(String text) {
		this.text = text;
		font.setText(text);
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
