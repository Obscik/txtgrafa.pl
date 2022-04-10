package me.deftware.installer.screen.impl;

import me.deftware.installer.Main;
import me.deftware.installer.engine.theming.DefaultThemes;
import me.deftware.installer.engine.theming.ThemeEngine;
import me.deftware.installer.screen.AbstractScreen;
import me.deftware.installer.screen.components.*;

/**
 * A demo screen used to demonstrate all the various components
 *
 * @author Deftware
 */
public class DemoScreen extends AbstractScreen {

	/*
		This function is called every time this screen is initialized, happens every time the theme is changed for example
	 */
	@Override
	public void init() {
		// We must clear the component list prior to doing anything else
		componentList.clear();

		/*
			Label
		 */
		// Casting to the component is only required if you call any center function after the constructor
		TextComponent textComponent = (TextComponent) new TextComponent(0, 40, 40, "A demo of components").centerHorizontally();
		// We can add onClick consumers to any label
		textComponent.setClickCallback(mouseButton -> {
			System.out.println("Label clicked with mouse button " + mouseButton);
		});
		textComponent.setTooltip("Multiline/nTooltip/nTest");

		/*
			TextBox
		 */
		// The .centerHorizontally() function can be used to center any component, there is also centerVertically, and .center() which does both
		// All components which have text can have their font size changed in the constructor, recommended for most is 30
		TextBoxComponent textBoxComponent = (TextBoxComponent) new TextBoxComponent(0, 120, 600, 30, "").centerHorizontally();
		// You can set a shadow text that only appears when no text is entered
		textBoxComponent.setShadowText("Click me and enter some text...");
		// We can also set callbacks for when text is changed
		textBoxComponent.setOnChangedCallback(text -> {
			System.out.println("Text set to " + text);
		});

		/*
			Browsable textbox, allows a user to click a button to open a folder browse dialog and sets the text to the chosen path
		 */
		// Dynamically set the Y location from the previous component + some padding (10 is recommended)
		BrowsableTextBoxComponent browsableTextBoxComponent = (BrowsableTextBoxComponent) new BrowsableTextBoxComponent(0, textBoxComponent.getY() + textBoxComponent.getHeight() + 10, 600, 30, "").centerHorizontally();
		// By default a BrowsableTextBoxComponent is readonly, you can toggle it with
		browsableTextBoxComponent.setReadOnly(false);
		// A BrowsableTextBoxComponent extends TextBoxComponent, so you can use any method from there as well
		browsableTextBoxComponent.setShadowText("Click the ... to select a path");

		// You can set the text of both textboxes above ^^ using .setText and get it with .getText

		/*
			ComboBox
		 */
		// When constructing the combobox, you can pass any number of strings into it for the items
		ComboBoxComponent comboBoxComponent = (ComboBoxComponent) new ComboBoxComponent(0, browsableTextBoxComponent.getY() + browsableTextBoxComponent.getHeight() + 10, 600, 30, "Hello", "World", "Item 3").centerHorizontally();
		// You can also change them at runtime using
		comboBoxComponent.updateItems("Item 1", "Item 2", "Item 3");
		// You can also set a callback for when the selected item is changed
		comboBoxComponent.setItemChangedCallback(selectedItem -> {
			System.out.println("Selected item " + selectedItem);
		});

		/*
			Checkbox
		 */
		CheckBoxComponent checkBoxComponent = (CheckBoxComponent) new CheckBoxComponent(0, comboBoxComponent.getY() + comboBoxComponent.getHeight() + 10, "Check me!", 30).centerHorizontally();
		// We can also add a callback to this component
		checkBoxComponent.setOnCheckCallback(checked -> {
			System.out.println("Checkbox was set to " + checked);
		});
		// We can also read the status of it with
		System.out.println(checkBoxComponent.isChecked());

		/*
			Image
		 */
		// We can specify a scale to scale down the image to a size that fits, we can also set a callback for when the image is clicked
		TextureComponent textureComponent = (TextureComponent) new TextureComponent(0, comboBoxComponent.getY() + comboBoxComponent.getHeight() + 10, "/assets/logo.png", 15, mouseButton -> {
			System.out.println("Image clicked!");
			// When using centerHorizontally or centerVertically we can also specify an offset which it will add to the x or y location
		}).centerHorizontally(200);

		/*
			Button
		 */
		// Default text size on a button is 30
		ButtonComponent themeButton = new ButtonComponent(0, Main.getWindow().windowHeight - 100, 100, 50, "Theme", mouseClicked -> {
			if (ThemeEngine.getTheme() == DefaultThemes.BLUE) {
				// The theme can be changed like this, all components will automatically update
				// You can also create a new theme by implementing a class with ITheme
				ThemeEngine.setTheme(DefaultThemes.WHITE);
			} else {
				ThemeEngine.setTheme(DefaultThemes.BLUE);
			}
		}), exitButton = new ButtonComponent(0, Main.getWindow().windowHeight - 100, 100, 50, "Exit", mouseClicked -> {
			Main.getWindow().close();
		});
		// We can add multiple callbacks to a button click as well, to dynamically update tooltips for example
		themeButton.getOnClickCallbacks().add(mouseButton -> {
			themeButton.setTooltip(ThemeEngine.getTheme().getName());
		});

		// Two or more buttons can be centered next to each other by using the offset
		themeButton.centerHorizontally(-100);
		exitButton.centerHorizontally(100);

		/*
			The addComponent can accept an unlimited amount of components
			Note: You must add the components starting from the top of the screen down, in order for the
			Z level to work properly, as items are rendered backwards.
		 */
		addComponent(textComponent, textBoxComponent, browsableTextBoxComponent, comboBoxComponent, checkBoxComponent, textureComponent, themeButton);

		// We can also just add one...
		addComponent(exitButton);
		// But they must still be in order as said previously for Z level to work

	}

}
