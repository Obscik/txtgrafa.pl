package me.deftware.installer.screen;

import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;
import me.deftware.installer.engine.MainWindow;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.screen.components.TextComponent;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Deftware
 */
public abstract class AbstractScreen {

	private TextComponent tooltipFont;
	private @Getter @Setter int x = 0, y = 0;
	protected @Getter List<AbstractComponent<?>> componentList = new ArrayList<>();
	protected @Getter @Setter boolean initialized = false;

	protected void addComponent(AbstractComponent<?>... components) {
		componentList.addAll(Arrays.asList(components));
	}

	public void render(double mouseX, double mouseY) {
		String tooltip = "";
		// Main render
		int cursor = GLFW.GLFW_ARROW_CURSOR;
		for (int i = componentList.size() - 1; i > -1; i--) {
			AbstractComponent<?> component = componentList.get(i);
			if (component.isVisible()) {
				component.render(x + component.getX(), y + component.getY(), mouseX, mouseY);
				if (mouseX > component.getX() && mouseX < component.getX() + component.getWidth() && mouseY > component.getY() && mouseY < component.getY() + component.getHeight()) {
					if (!component.getTooltip().isEmpty()) {
						tooltip = component.getTooltip();
					}
				}
				if (cursor == GLFW.GLFW_ARROW_CURSOR) {
					cursor = component.cursorTest(mouseX, mouseY);
				}
			}
		}
		if (!MainWindow.getCursorCache().containsKey(cursor)) {
			MainWindow.getCursorCache().put(cursor, GLFW.glfwCreateStandardCursor(cursor));
		}
		GLFW.glfwSetCursor(Main.getWindow().getWindowHandle(), MainWindow.getCursorCache().get(cursor));
		// Tooltip
		if (!tooltip.isEmpty() && tooltipFont != null) {
			// Draw tooltip
			float offset = 20, textOffset = 3, textWidth = tooltipFont.getStringWidth(tooltip), textHeight = tooltipFont.getHeight();
			if (tooltip.contains("/n")) {
				textWidth = 0;
				for (String line : tooltip.split("/n")) {
					if (textWidth < tooltipFont.getStringWidth(line)) {
						textWidth = tooltipFont.getStringWidth(line);
					}
				}
				textHeight *= tooltip.split("/n").length;
			}
			// Shadow
			RenderSystem.drawRect((float) mouseX + offset - textOffset + 2, (float) mouseY + offset + 2, (float) mouseX + offset + textWidth + (textOffset * 2) + 2, (float) mouseY + offset + textHeight + 2, ThemeEngine.getTheme().getTooltipBackground().darker());
			// Box
			RenderSystem.drawRect((float) mouseX + offset - textOffset, (float) mouseY + offset, (float) mouseX + offset + textWidth + (textOffset * 2), (float) mouseY + offset + textHeight, ThemeEngine.getTheme().getTooltipBackground());
			tooltipFont.drawString((float) mouseX + offset + textOffset, (float) mouseY + offset, ThemeEngine.getTheme().getTextColor(), tooltip.contains("/n") ? tooltip.split("/n") : new String[]{ tooltip });
		}
	}

	public void update() {
		for (AbstractComponent<?> component : componentList) {
			component.update();
		}
	}

	public void mouseClicked(double x, double y, int button) {
		boolean deFocus = false;
		for (AbstractComponent<?> component : componentList) {
			if (!deFocus && component.mouseClicked(x, y, button)) {
				deFocus = true;
			} else if (deFocus) {
				component.deFocus();
			}
		}
	}

	public void mouseReleased(double x, double y, int button) {
		for (AbstractComponent<?> component : componentList) {
			component.mouseReleased(x, y, button);
		}
	}

	public void onScroll(double xPos, double yPos) {
		for (AbstractComponent<?> component : componentList) {
			component.onScroll(xPos, yPos);
		}
	}

	public void keyPressed(int keyCode, int mods) {
		for (AbstractComponent<?> component : componentList) {
			component.keyPressed(keyCode, mods);
		}
	}

	public void charTyped(char charTyped) {
		for (AbstractComponent<?> component : componentList) {
			component.charTyped(charTyped);
		}
	}

	public static void openLink(String link) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(link));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onWindowResize() {
		init();
	}

	public static float getWindowWidth() {
		return Main.window.windowWidth;
	}

	public static float getWindowHeight() {
		return Main.window.windowHeight;
	}

	public abstract void init();

	public void initSuper() {
		if (!initialized) {
			initialized = true;
			tooltipFont = new TextComponent(0, 0, 30, "ABC");
			tooltipFont.setCenteredText(false);
			init();
		}
	}
}
