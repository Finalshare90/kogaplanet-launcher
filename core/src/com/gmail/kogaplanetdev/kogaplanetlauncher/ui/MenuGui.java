package com.gmail.kogaplanetdev.kogaplanetlauncher.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.MapDrawer;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.Player;

public class MenuGui {
	
	private Skin menuSkin;
	private Table menuGroup,fpsTable;
	private Boolean isClicked = true, isFpsActive = false;
	private Button resetButton, leaveButton;
	private CheckBox fpsCheckBox;
	private Label currentMap, menuLabel;
	private BitmapFont font;
	private Player player;
	
	
	public MenuGui(Skin menuSkin, Stage stage, Table fpsTable, Player player){
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PrStart.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 18;
		font = generator.generateFont(parameter);
		
		this.menuSkin = menuSkin;
		this.fpsTable = fpsTable;
		this.player = player;
		menuSkin.add("font", font);
		createMenu();
		stage.addActor(menuGroup);

	}

	private void createMenu(){
		
		menuGroup = new Table(menuSkin);
		menuGroup.background("Windows/menu");
		menuGroup.setSize(whatValueIsPercentage(47, Gdx.graphics.getWidth()), whatValueIsPercentage(70, Gdx.graphics.getHeight()));
		menuGroup.setVisible(false);
		menuGroup.clip(true);
		
		
		resetButton = createButton(menuSkin, "Buttons/reset", "Buttons/reset_selected");
		menuGroup.add(resetButton)
		.size(whatValueIsPercentage(14, Gdx.graphics.getWidth()), whatValueIsPercentage(5, Gdx.graphics.getHeight()))
		.padLeft(whatValueIsPercentage(14, Gdx.graphics.getWidth()))
		.padBottom(whatValueIsPercentage(-61, Gdx.graphics.getHeight()));
		resetButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				player.isAlive(false);
				setClicked();
			}		
		});
		
		fpsCheckBox = createCheckbox(menuSkin, "Buttons/check_off", "Buttons/check_on", "Show FPS");
		fpsCheckBox.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				isFpsActive(fpsTable);
			}
		});
		
		menuGroup.add(fpsCheckBox).setActorBounds(whatValueIsPercentage(14, Gdx.graphics.getWidth()), 
												  whatValueIsPercentage(26, Gdx.graphics.getHeight()), 
												  whatValueIsPercentage(4, Gdx.graphics.getWidth()), 
												  whatValueIsPercentage(8, Gdx.graphics.getHeight()));
		
		
		leaveButton = createButton(menuSkin, "Buttons/leave", "Buttons/leave_selected");
		menuGroup.add(leaveButton)
		.size(whatValueIsPercentage(14, Gdx.graphics.getWidth()), whatValueIsPercentage(5, Gdx.graphics.getHeight()))
		.padRight(whatValueIsPercentage(14, Gdx.graphics.getWidth()))
		.padBottom(whatValueIsPercentage(-61, Gdx.graphics.getHeight()));
		leaveButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				System.exit(0);}
			});
		
		
		currentMap = createLabel("Current Map: " + MapDrawer.mapName, menuSkin);
		menuGroup.add(currentMap)
		.size(whatValueIsPercentage(29, Gdx.graphics.getWidth()), whatValueIsPercentage(7, Gdx.graphics.getHeight()))
		.padTop(whatValueIsPercentage(13, Gdx.graphics.getHeight()))
		.padLeft(whatValueIsPercentage(-73, Gdx.graphics.getWidth()));
		
		menuLabel = createLabel("Game Menu", menuSkin);
		menuGroup.add(menuLabel)
		.size(whatValueIsPercentage(7, Gdx.graphics.getWidth()), 
				whatValueIsPercentage(7, Gdx.graphics.getHeight()))
		.padTop(whatValueIsPercentage(-22, Gdx.graphics.getHeight()))
		.padLeft(whatValueIsPercentage(-78, Gdx.graphics.getWidth()))
		.top();
	}
	
	private Button createButton(Skin uiSkin, String up, String down){
		Drawable upDrawable = uiSkin.newDrawable(up);
		Drawable downDrawable = uiSkin.newDrawable(down);
		Button button = new Button(upDrawable, downDrawable);
		return button;
	}
	
	private Label createLabel (String value, Skin uiSkin){
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = uiSkin.getFont("font");
		Label label = new Label(value, labelStyle);
		return label;
	}
	
	public void setClicked(){
		menuGroup.setVisible(isClicked);
		isClicked = !isClicked;
	}
	
	private CheckBox createCheckbox(Skin uiSkin, String up, String down, String text) {
		
		Drawable upDrawable = uiSkin.newDrawable(up);
		Drawable downDrawable = uiSkin.newDrawable(down);
		
		CheckBoxStyle style = new CheckBoxStyle();
		style.checkboxOff = downDrawable;
		style.checkboxOn = upDrawable;
		style.font = font;
		
		return new CheckBox(text, style);
	}
	
	public boolean isFpsActive(Table table) {
		table.setVisible(isFpsActive);
		isFpsActive = !isFpsActive;
		return isFpsActive;
	}
	
	private float whatValueIsPercentage(float percentage, float maxValue100)
	{
		return (percentage * maxValue100) / 100;
	}

	public void dispose(){
		font.dispose();
		menuSkin.dispose();
	}
	
	public Actor getGroup(){
		return menuGroup;
	}	
}
