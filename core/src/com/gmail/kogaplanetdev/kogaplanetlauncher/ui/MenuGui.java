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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.MapDrawer;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.Player;

public class MenuGui {
	
	Skin menuSkin;
	Table menuGroup,fpsTable;
	private Boolean isClicked = true, isFpsActive = false;
	Button resetButton, leaveButton;
	CheckBox fpsCheckBox;
	Label currentMap, menuLabel;
	BitmapFont font;
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
		menuGroup.setSize(900, 600);
		menuGroup.setVisible(false);
		menuGroup.clip(true);
		
		
		resetButton = createButton(menuSkin, "Buttons/reset", "Buttons/reset_selected");
		menuGroup.add(resetButton)
		.size(300,50)
		.padLeft(50)
		.padBottom(-500);
		resetButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				player.isAlive(false);
				isClicked();
			}		
		});
		
		
		leaveButton = createButton(menuSkin, "Buttons/leave", "Buttons/leave_selected");
		menuGroup.add(leaveButton)
		.size(300,50)
		.space(50)
		.padBottom(-500);
		leaveButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				System.exit(0);}
			});
		
		
		currentMap = createLabel("Current Map: " + MapDrawer.mapName, menuSkin);
		menuGroup.add(currentMap)
		.size(500, 100)
		.padTop(-400)
		.padLeft(-600);
		
		menuLabel = createLabel("Game Menu", menuSkin);
		menuGroup.add(menuLabel)
		.size(90, 50)
		.padTop(-300)
		.padLeft(-800)
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
	
	public void isClicked(){
		menuGroup.setVisible(isClicked);
		isClicked = !isClicked;
	}
	
	public boolean isFpsActive(Table table) {
		table.setVisible(isFpsActive);
		isFpsActive = !isFpsActive;
		return isFpsActive;
	}

	public Actor getGroup(){
		return menuGroup;
	}	
}
