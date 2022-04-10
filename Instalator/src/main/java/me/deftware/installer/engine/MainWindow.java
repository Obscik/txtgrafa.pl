package me.deftware.installer.engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Getter;
import lombok.Setter;
import me.deftware.aristois.installer.InstallerAPI;
import me.deftware.aristois.installer.ui.InstallerUI;
import me.deftware.installer.Main;
import me.deftware.installer.OSUtils;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.resources.RenderSystem;
import me.deftware.installer.resources.font.BitmapFont;
import me.deftware.installer.resources.font.FontManager;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.impl.DemoScreen;
import me.deftware.installer.screen.impl.WelcomeScreen;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Deftware
 */
@SuppressWarnings("FieldMayBeFinal")
public class MainWindow implements Runnable {

	public @Getter long windowHandle;
	public @Getter double mouseX, mouseY;
	public static @Getter boolean borderlessWindow = true, legacyOpen = false;
	public int windowWidth = 800, windowHeight = 500;
	private boolean transitionForward = true;
	private double iterations = 50, i = 0, counter = 0, increase = Math.PI / iterations;
	private DoubleBuffer posX = BufferUtils.createDoubleBuffer(1), posY = BufferUtils.createDoubleBuffer(1);
	public AbstractScreen currentScreen, transitionScreen, previousScreen;
	private @Getter WindowDecorations windowDecorations;
	private List<Runnable> renderThreadRunner = new ArrayList<>();
	private @Setter boolean scheduleRefresh = false;
	private @Getter static HashMap<Integer, Long> cursorCache = new HashMap<>();

	/**
	 * Set to false for things like opening dialogs
	 */
	public boolean shouldRun = true;

	@Override
	public void run() {
		if (OSUtils.isLinux()) {
			// Does not work with Wayland
			borderlessWindow = false;
		}

		// 60 times per second
		ScheduledFuture<?> updatedThread = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
			if (currentScreen != null) {
				currentScreen.update();
			}
			if (transitionScreen != null) {
				handleTransition();
			}
		}, 0, 1000 / 60, TimeUnit.MILLISECONDS);

		init();
		loop();

		updatedThread.cancel(true);
		Callbacks.glfwFreeCallbacks(windowHandle);
		GLFW.glfwDestroyWindow(windowHandle);

		GLFW.glfwTerminate();
		Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
		if (!legacyOpen) {
			System.exit(0);
		}
	}

	private void handleTransition() {
		if (transitionScreen.getX() > 0 && transitionForward || transitionScreen.getX() < 0 && !transitionForward) {
			if (i <= 1) {
				double transition = Math.sin(counter) * (windowWidth / iterations * counter);
				if (transitionForward) {
					transitionScreen.setX((int) (transitionScreen.getX() - transition));
				} else {
					transitionScreen.setX((int) (transitionScreen.getX() + transition));
				}
				counter += increase;
				i += 1 / iterations;
			}
			if (transitionForward) {
				currentScreen.setX(transitionScreen.getX() - windowWidth);
			} else {
				currentScreen.setX(transitionScreen.getX() + windowWidth);
			}
		} else {
			currentScreen = transitionScreen;
			currentScreen.setX(0);
			transitionScreen = null;
		}
	}

	public void transitionForward(AbstractScreen screen) {
		previousScreen = currentScreen;
		renderThreadRunner.add(() -> {
			screen.initSuper();
			transitionForward = true;
			counter = 0;
			i = 0;
			screen.setX(windowWidth);
			transitionScreen = screen;
		});
	}

	public void transitionBackwards(AbstractScreen screen) {
		previousScreen = currentScreen;
		renderThreadRunner.add(() -> {
			screen.initSuper();
			transitionForward = false;
			counter = 0;
			i = 0;
			screen.setX(-windowWidth);
			transitionScreen = screen;
		});
	}

	public boolean isTransitioning() {
		return transitionScreen != null;
	}

	public static void openLegacy() {

	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit()) {
			System.err.println("Failed to init glfw");
			openLegacy();
			return;
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		if (borderlessWindow) {
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
		}

		windowHandle = GLFW.glfwCreateWindow(windowWidth, windowHeight, InstallerAPI.isDonorBuild() ? "Donor edition" : "", MemoryUtil.NULL, MemoryUtil.NULL);
		if (windowHandle == MemoryUtil.NULL) {
			System.err.println("NULL window handle");
			openLegacy();
			return;
		}

		try {
			PNGDecoder decoder = new PNGDecoder(Main.class.getResourceAsStream("/assets/logo.png"));
			ByteBuffer buf = BufferUtils.createByteBuffer(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			buf.flip();

			GLFWImage image = GLFWImage.malloc();
			image.set(decoder.getWidth(), decoder.getHeight(), buf);
			GLFWImage.Buffer images = GLFWImage.malloc(1);

			GLFW.glfwSetWindowIcon(windowHandle, images.put(0, image));

			images.free();
			image.free();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			if (currentScreen != null && action != GLFW.GLFW_RELEASE && shouldRun) {
				currentScreen.keyPressed(key, mods);
			}
		});

		GLFW.glfwSetCharCallback(windowHandle, (window, codepoint) -> {
			if (currentScreen != null && shouldRun) {
				currentScreen.charTyped((char) codepoint);
			}
		});

		GLFW.glfwSetWindowCloseCallback(windowHandle, window -> {
			GLFW.glfwSetWindowShouldClose(windowHandle, true);
		});

		GLFW.glfwSetScrollCallback(windowHandle, (window, xPos, yPos) -> {
			if (currentScreen != null && shouldRun) {
				currentScreen.onScroll(xPos, yPos);
			}
		});

		if (!borderlessWindow) {
			GLFW.glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
				mousePressed(button, action);
			});
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1),  pHeight = stack.mallocInt(1);

			GLFW.glfwGetWindowSize(windowHandle, pWidth, pHeight);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
					windowHandle,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		}

		GLFW.glfwMakeContextCurrent(windowHandle);
		GLFW.glfwSwapInterval(1); // Enable v-sync
		GLFW.glfwShowWindow(windowHandle);
	}

	public void mousePressed(int button, int action) {
		if (currentScreen != null && !isTransitioning() && shouldRun) {
			if (action == GLFW.GLFW_PRESS) {
				currentScreen.mouseClicked(mouseX, mouseY, button);
			} else if (action == GLFW.GLFW_RELEASE) {
				currentScreen.mouseReleased(mouseX, mouseY, button);
			}
		}
	}

	public void close() {
		GLFW.glfwSetWindowShouldClose(Main.getWindow().getWindowHandle(), true);
	}

	private void setupView() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, windowWidth, windowHeight, 0.0, -1.0, 1.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void loop() {
		GL.createCapabilities();
		setupView();

		BitmapFont font = FontManager.getFont("NotoSans-Regular", 18, FontManager.Modifiers.ANTIALIASED);
		font.setShadowSize(1);
		font.initialize(Color.white, "");

		if (borderlessWindow) {
			windowDecorations = new WindowDecorations(InstallerAPI.isDonorBuild() ? "Donor edition" : "");
		}

		currentScreen = new WelcomeScreen();
		currentScreen.initSuper();
		while (!GLFW.glfwWindowShouldClose(windowHandle)) {
			RenderSystem.glClearColor(ThemeEngine.getTheme().getBackgroundColor());
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GLFW.glfwGetCursorPos(windowHandle, posX, posY);
			mouseX = posX.get();
			mouseY = posY.get();
			posX.clear();
			posY.clear();

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			if (!renderThreadRunner.isEmpty()) {
				for (Runnable runnable : renderThreadRunner) {
					runnable.run();
				}
				renderThreadRunner.clear();
			}

			if (currentScreen != null) {
				if (scheduleRefresh) {
					scheduleRefresh = false;
					currentScreen.setInitialized(false);
					currentScreen.initSuper();
				}
				currentScreen.render(mouseX, mouseY);
			}
			if (transitionScreen != null) {
				transitionScreen.render(mouseX, mouseY);
			}
			font.drawString(4, windowHeight - font.getStringHeight("ABC") - 2, "txtgrafa.pl");

			if (borderlessWindow){
				windowDecorations.loop();
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);

			GLFW.glfwSwapBuffers(windowHandle);
			GLFW.glfwPollEvents();
		}
	}

}
