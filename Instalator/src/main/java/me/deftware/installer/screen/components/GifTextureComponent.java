package me.deftware.installer.screen.components;

import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import me.deftware.installer.Main;
import me.deftware.installer.resources.GifDecoder;
import me.deftware.installer.resources.ResourceUtils;
import me.deftware.installer.resources.Texture;
import me.deftware.installer.screen.AbstractComponent;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A texture component which can be used to render gifs in a screen
 *
 * @author Deftware
 */
public class GifTextureComponent extends AbstractComponent<GifTextureComponent> {

	private final List<GifSlice> slices = new ArrayList<>();
	private int delay = 0, index = 0;
	private @Getter float width, height;
	private @Getter @Setter int scale, speedOverride;
	private boolean fillScreen = false;
	private final String asset;

	public GifTextureComponent(float x, float y, String asset, int scale, int speedOverride) {
		super(x, y);
		this.scale = scale;
		this.speedOverride = speedOverride;
		this.asset = asset;
	}

	public GifTextureComponent init() {
		try {
			GifDecoder.GifImage gif = GifDecoder.read(ByteStreams.toByteArray(ResourceUtils.getStreamFromResources(asset)));
			for (int i = 0; i < gif.getFrameCount(); i++) {
				BufferedImage img = gif.getFrame(i);
				width = img.getWidth() / scale;
				height = img.getHeight() / scale;
				slices.add(new GifSlice(img, gif.getDelay(i), i));
			}
			if (fillScreen) {
				width = Main.getWindow().windowWidth;
				height = Main.getWindow().windowHeight;
			}
			Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
				if (delay == slices.get(index).delay) {
					delay = 0;
					index = index == slices.size() - 1 ? 0 : index + 1;
					return;
				}
				delay++;
			}, 0, 10, TimeUnit.MILLISECONDS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public GifTextureComponent setFillScreen(boolean fillScreen) {
		this.fillScreen = fillScreen;
		return this;
	}

	@Override
	public void render(float x, float y, double mouseX, double mouseY) {
		slices.get(index).render(x, y);
	}

	@Override
	public void update() { }

	@Override
	public boolean mouseClicked(double x, double y, int mouseButton) {
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

	private class GifSlice {

		Texture texture;
		int delay, index;

		GifSlice(BufferedImage texture, int delay, int index) {
			if (fillScreen) {
				this.texture = ResourceUtils.loadTextureFromBufferedImage(texture, Main.getWindow().windowWidth, Main.getWindow().windowHeight);
			} else {
				this.texture = ResourceUtils.loadTextureFromBufferedImage(texture, scale);
			}
			this.delay = speedOverride == -1 ? delay : speedOverride;
			this.index = index;
		}

		void render(float x, float y) {
			try {
				texture.draw(x, y);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}
