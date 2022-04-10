package me.deftware.installer.resources.font;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Texture loading management
 *
 * @author Deftware
 */
public class GraphicsUtil {

	public static int loadTextureFromBufferedImage(BufferedImage image) {
		return loadTextureFromBufferedImage(image, 4, image.getWidth(), image.getHeight());
	}

	public static int loadTextureFromBufferedImage(BufferedImage image, int bytesPerPixel, int width, int height) {
		return loadTextureFromBufferedImage(image, bytesPerPixel, width, height, 33071, 33071);
	}

	public static int loadTextureFromBufferedImage(BufferedImage image, int bytesPerPixel, int width, int height, int clampModeS, int clampModeT) {
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, image.getWidth());
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * bytesPerPixel);

		for(int y = 0; y < image.getHeight(); ++y) {
			for(int x = 0; x < image.getWidth(); ++x) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte)(pixel >> 16 & 255));
				buffer.put((byte)(pixel >> 8 & 255));
				buffer.put((byte)(pixel & 255));
				if (bytesPerPixel == 4) {
					buffer.put((byte)(pixel >> 24 & 255));
				}
			}
		}

		buffer.flip();
		return loadTexture(buffer, image.getWidth(), image.getHeight(), clampModeS, clampModeT);
	}

	public static int loadTexture(ByteBuffer bytebuffer, int width, int height, int clampModeS, int clampModeT) {
		int textureID = GL11.glGenTextures();
		GL13.glActiveTexture(33984);
		GL11.glBindTexture(3553, textureID);
		GL11.glTexParameteri(3553, 10242, clampModeS);
		GL11.glTexParameteri(3553, 10243, clampModeT);
		GL11.glTexParameteri(3553, 10241, 9729);
		GL11.glTexParameteri(3553, 10240, 9729);
		GL11.glPixelStorei(3315, 0);
		GL11.glPixelStorei(3316, 0);
		GL11.glPixelStorei(3317, 4);
		GL11.glTexImage2D(3553, 0, 32856, width, height, 0, 6408, 5121, bytebuffer);
		return textureID;
	}

	public static void prepareMatrix(int matrixWidth, int matrixHeight) {
		matrixWidth = matrixWidth > 0 ? matrixWidth : 10;
		matrixHeight = matrixHeight > 0 ? matrixHeight : 10;
		GL11.glMatrixMode(5888);
		GL11.glEnable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glMatrixMode(5889);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)matrixWidth, (double)matrixHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(5888);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void drawQuads(int x, int y, int width, int height) {
		GL11.glBegin(7);
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex2d((double)x, (double)y);
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex2d((double)x, (double)(y + height));
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex2d((double)(x + width), (double)(y + height));
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex2d((double)(x + width), (double)y);
		GL11.glEnd();
	}

}

