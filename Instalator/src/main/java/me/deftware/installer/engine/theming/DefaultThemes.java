package me.deftware.installer.engine.theming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

/**
 * A collection of default themes that can be used
 *
 * @author Deftware
 */
public @AllArgsConstructor enum DefaultThemes implements ITheme {

	/**
	 * The default theme used, with the Noto Sans font included in the assets of the jar
	 */
	BLUE(new Color(19, 29, 39), new Color(36, 58, 82), new Color(36, 58, 82).brighter().brighter(), new Color(36, 58, 82).brighter(), Color.white, Color.white, Color.white.darker(), Color.black,"NotoSans-Regular", "Blue", true),

	/*
		Other themes
	 */
	WHITE(Color.white, Color.white.darker(), Color.gray.brighter(), Color.gray, Color.black, Color.black, Color.gray, Color.white.darker(), "NotoSans-Regular", "White", false);

	/*
		Various fields of the default colors, which can be modified at runtime
	 */
	private @Getter @Setter Color backgroundColor, foregroundColor, scrollerColor, scrollerBackgroundColor, outlineColor, textColor, textHighlightColor, tooltipBackground;
	private @Setter @Getter String textFont, name;
	private @Getter @Setter boolean textShadow;

}
