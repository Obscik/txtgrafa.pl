package me.deftware.installer.engine.theming;

import java.awt.*;

/**
 * The main theme interface, to create a new theme implement a class by this interface
 *
 * @author Deftware
 */
public interface ITheme {

	/**
	 * The main background
	 */
	Color getBackgroundColor();

	/**
	 * A brighter version of the background color ideally, used for highlighting components and the like
	 */
	Color getForegroundColor();

	/**
	 * The color of the scroller in the scrollbar
	 */
	Color getScrollerColor();

	/**
	 * The color of the scroller background
	 */
	Color getScrollerBackgroundColor();

	/**
	 * The color used for outlining in components
	 */
	Color getOutlineColor();

	/**
	 * The color of text components
	 */
	Color getTextColor();

	/**
	 * The highlighted color of text in a textbox
	 */
	Color getTextHighlightColor();

	/**
	 * The background of tooltip popups
	 */
	Color getTooltipBackground();

	/**
	 * If text should have shadow by default
	 */
	boolean isTextShadow();

	/**
	 * The font used throughout various components
	 * Custom .ttf fonts can be used, however, they must be pre-loaded and stored in the
	 * jars /assets/ folder
	 */
	String getTextFont();

	/**
	 * The name of the theme
	 */
	String getName();

}
