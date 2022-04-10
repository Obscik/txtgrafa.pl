package me.deftware.installer.resources;

import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Various functions for helping with rendering
 *
 * @author Deftware
 */
public class RenderSystem {

	/**
	 * Sets the current gl color
	 */
	public static void glColor(Color color) {
		GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
	}

	/**
	 * Clears the current color with a new color
	 */
	public static void glClearColor(Color color) {
		GL11.glClearColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
	}

	/**
	 * Draws a rectangle in a specified color, note that this function does not draw x + width, y + height,
	 * instead, it draws from point (x, y) to (x2, y2), x2 would be equal to x + width and y2 y + height
	 */
	public static void drawRect(float x, float y, float x2, float y2, Color color) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		glColor(color);
		GL11.glBegin(7);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		glColor(Color.white);
	}

	/**
	 * Draws a line between two points
	 */
	public static void drawLine(float x1, float y1, float x2, float y2, Color color) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glLineWidth(1.0F);
		glColor(color);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		glColor(Color.white);
	}

	/**
	 * Draws a circle at a given point
	 */
	public static void drawCircle(float xx, float yy, float radius, Color color) {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glBegin(6);
		for (int i = 0; i < 50; i++) {
			float x = (float) (radius * Math.sin(i * 0.12566370614359174D));
			float y = (float) (radius * Math.cos(i * 0.12566370614359174D));
			glColor(color);
			GL11.glVertex2f(xx + x, yy + y);
		}
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
		glColor(Color.white);
	}

}
