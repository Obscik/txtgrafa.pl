package me.deftware.installer.resources;

import de.matthiasmann.twl.utils.PNGDecoder;
import me.deftware.installer.LocationUtil;
import me.deftware.installer.Main;
import me.deftware.installer.resources.font.GraphicsUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Deftware
 */
public class ResourceUtils {

	public static InputStream getStreamFromResources(String resourcePath) {
		String emcPath = Objects.requireNonNull(LocationUtil.getClassPhysicalLocation(Main.class).toFile()).getAbsolutePath();
		if (emcPath.contains("build")) {
			emcPath = emcPath.split("build")[0];
			try {
				return new FileInputStream(String.format("%s/src/main/resources/%s", emcPath, resourcePath).replace("/", File.separator));
			} catch (IOException e) {
				return null;
			}
		}
		try {
			if (resourcePath.startsWith("/")) {
				resourcePath = resourcePath.substring(1);
			}
			ZipFile zipFile = new ZipFile(LocationUtil.getClassPhysicalLocation(Main.class).toFile());
			ZipEntry entry = zipFile.getEntry(resourcePath);
			InputStream in = zipFile.getInputStream(entry);
			return in;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Texture loadTexture(String fileName) throws Exception {
		return loadTexture(fileName, 1);
	}

	public static Texture loadTextureFromBufferedImage(BufferedImage image, float width, float height) {
		return new Texture(GraphicsUtil.loadTextureFromBufferedImage(image), width, height, 1f);
	}

	public static Texture loadTextureFromBufferedImage(BufferedImage image, float scale) {
		return new Texture(GraphicsUtil.loadTextureFromBufferedImage(image), image.getWidth() / scale, image.getHeight() / scale, scale);
	}

	public static Texture loadTexture(String fileName, float scale) throws Exception {
		PNGDecoder decoder = new PNGDecoder(Main.class.getResourceAsStream(fileName));
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
		decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
		buffer.flip();

		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		return new Texture(id, decoder.getWidth() / scale, decoder.getHeight() / scale, scale);
	}

}
