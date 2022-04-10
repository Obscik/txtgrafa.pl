package me.deftware.installer.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.lwjgl.opengl.GL11;

/**
 * Represents a texture and can load/draw it
 *
 * @author Deftware
 */
public @Data @AllArgsConstructor class Texture {

	private int id;
	private float width, height, scale;

	public void draw(float x, float y) throws Exception {
		draw(x, y, 1f);
	}

	public void draw(float x, float y, float textureScale) throws Exception {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getId());
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(getWidth() / textureScale, 0);

			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(getWidth() / textureScale, getHeight() / textureScale);

			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0, getHeight() / textureScale);
		}

		GL11.glEnd();
		GL11.glPopMatrix();
	}

}
