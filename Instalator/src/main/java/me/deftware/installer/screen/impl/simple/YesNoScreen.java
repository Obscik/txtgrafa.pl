package me.deftware.installer.screen.impl.simple;

import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.ButtonComponent;
import me.deftware.installer.screen.components.TextComponent;

import java.util.function.Consumer;

/**
 * @author Deftware
 */
public class YesNoScreen extends AbstractScreen {

	private String rightButton = "Continue", leftButton = "Go back";

	private String[] text;
	private String title;
	private Consumer<Boolean> callback;

	public YesNoScreen(String title, Consumer<Boolean> callback, String... text) {
		this.title = title;
		this.text = text;
		this.callback = callback;
	}

	public YesNoScreen setRightButton(String rightButton) {
		this.rightButton = rightButton;
		return this;
	}

	public YesNoScreen setLeftButton(String leftButton) {
		this.leftButton = leftButton;
		return this;
	}

	@Override
	public void init() {
		componentList.clear();
		TextComponent titleComponent = new TextComponent(0, 0,  35, title);
		titleComponent.centerHorizontally().centerVertically(-130);
		TextComponent subText = new TextComponent(0, 0,  25, text);
		subText.centerHorizontally();
		subText.setY(titleComponent.getY() + titleComponent.getHeight() + 10);
		addComponent(titleComponent, subText);
		addComponent(new ButtonComponent(0, 0, 100, 50, leftButton, mouseButton -> callback.accept(false)).centerVertically(170).centerHorizontally(-100));
		addComponent(new ButtonComponent(0, 0, 100, 50, rightButton, mouseButton -> callback.accept(true)).centerVertically(170).centerHorizontally(100));
	}

}
