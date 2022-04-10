package me.deftware.installer.screen.impl.simple;

import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.TextComponent;

import java.util.function.Consumer;

/**
 * @author Deftware
 */
public class TransitionScreen extends AbstractScreen {

	private String[] text;
	private String title;
	private Consumer<Boolean> callback;
	private int delay;

	public TransitionScreen(String title, Consumer<Boolean> callback, String... text) {
		this(title, callback, 1500, text);
	}

	public TransitionScreen(String title, Consumer<Boolean> callback, int delay, String... text) {
		this.title = title;
		this.text = text;
		this.callback = callback;
		this.delay = delay;
	}

	@Override
	public void init() {
		componentList.clear();
		TextComponent titleComponent = new TextComponent(0, 0,  35, title);
		titleComponent.centerHorizontally().centerVertically(-100);
		TextComponent subText = new TextComponent(0, 0,  25, text);
		subText.centerHorizontally();
		subText.setY(titleComponent.getY() + titleComponent.getHeight() + 10);
		subText.setAlpha(1);
		titleComponent.setAlpha(1);
		addComponent(titleComponent, subText);
		titleComponent.fadeIn(null);
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			subText.fadeIn(alpha -> {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				callback.accept(true);
			});
		}).start();
	}

}
