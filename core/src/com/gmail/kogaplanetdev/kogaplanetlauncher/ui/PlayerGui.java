package com.gmail.kogaplanetdev.kogaplanetlauncher.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gmail.kogaplanetdev.kogaplanetlauncher.KogaPlanetLauncher;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.Player;


public class PlayerGui{

	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;
	public Player player;
	private Boolean isPressedF1, showHitBoxes;
	
	Stage stage;	
	Table fpsTable;
	Container<Actor> buttonContainer, barContainer;
	
	TextureAtlas uiTexture = new TextureAtlas("misc/HarmonyUI.atlas");
	Skin uiSkin = new Skin(uiTexture);

	Label fpsLabel, positionLabel;
	LabelStyle labelStyle;
	
	Button menuButton = createButton(uiSkin, "Buttons/menu", "Buttons/menu_down");
	
	MenuGui menuGui;
	
	public PlayerGui(SpriteBatch batch, Player player) {	
		
		this.player = player;
		
		// Parte do sistema de hitbox, debug apenas
		showHitBoxes = false;
		
		// Gera a fonte PrStart, n�o � essencial, mas vale a pena usar =)
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PrStart.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		
	    font = generator.generateFont(parameter);
	    
	    
	    
	    stage = new Stage(this.player.getPlayerViewport(), batch);
	    Gdx.input.setInputProcessor(stage);
	    
	    fpsTable = new Table(uiSkin);
	    
	    labelStyle = new LabelStyle();
	    labelStyle.font = font;
	    
	    fpsLabel = createLabel(getFps(), labelStyle, fpsTable);
	    fpsTable.setSize(whatValueIsPercentage(7, Gdx.graphics.getWidth()), whatValueIsPercentage(3, Gdx.graphics.getHeight()));
	    stage.addActor(fpsTable);
	    
	    
	    positionLabel = createLabel(player.getX() + ", " + player.getY(), labelStyle, fpsTable);
	    positionLabel.setSize(whatValueIsPercentage(7, Gdx.graphics.getWidth()), whatValueIsPercentage(3, Gdx.graphics.getHeight()));
	    positionLabel.setVisible(false);
	    stage.addActor(positionLabel);
	    
	    buttonContainer = new Container<Actor>(); 
	    buttonContainer.setActor(menuButton);
	    buttonContainer.size(whatValueIsPercentage(4, Gdx.graphics.getWidth()), whatValueIsPercentage(7, Gdx.graphics.getHeight()));
	    
	    stage.addActor(buttonContainer);
	    
	    menuGui = new MenuGui(uiSkin, stage, fpsTable, player);
	    
	    menuButton.getClickListener();
	    //ButtonActionListener 
	    menuButton.addListener(new ClickListener(){
	    	
	    	@Override
	    	public void clicked(InputEvent event, float x, float y) {
	    		super.clicked(event, x, y);
	    		menuGui.setClicked();
	    		}
	    	}
	    );
	   
	    
	    barContainer = new Container<Actor>();
	    barContainer.setSize(whatValueIsPercentage(32, Gdx.graphics.getWidth()),whatValueIsPercentage(9, Gdx.graphics.getHeight()));
	    barContainer.background(uiSkin.newDrawable("Windows/top_bar"));
	    
	    stage.addActor(barContainer);
	    
	    
	    
	}
	private Button createButton(Skin buttonSkin, String upSkin, String downSkin){
		
		Drawable up = buttonSkin.newDrawable(upSkin);
		Drawable down = buttonSkin.newDrawable(downSkin);
		
		return new Button(up,down);
		
	}
	
	private Label createLabel(String value, LabelStyle labelSkin, Table group){
		Label label = new Label(value, labelSkin);
		group.add(label);
		return label;
	}
	
	private String getFps(){
		int fps = Gdx.graphics.getFramesPerSecond();
		String fpsToString = "FPS: " + Integer.toString(fps);
		return fpsToString;
	}
	
	public void update(SpriteBatch batch) {
		
		stageUpdate();		
		
		//FPS label update
		fpsLabel.setText(getFps());
		fpsTable.setPosition(player.getX() + whatValueIsPercentage(7, Gdx.graphics.getWidth()), player.getY() + whatValueIsPercentage(43, Gdx.graphics.getHeight()));
		
		positionLabel.setText(player.getX() + ", " + player.getY());
		positionLabel.setPosition(player.getCam().position.x + whatValueIsPercentage(29, Gdx.graphics.getWidth()), player.getCam().position.y + whatValueIsPercentage(45, Gdx.graphics.getHeight()));
		
		buttonContainer.setPosition(player.getX() - whatValueIsPercentage(13, Gdx.graphics.getWidth()), player.getY() + whatValueIsPercentage(44.5f, Gdx.graphics.getHeight()));
		
		barContainer.toBack();
		barContainer.setPosition(player.getX() - whatValueIsPercentage(17, Gdx.graphics.getWidth()), player.getY() + whatValueIsPercentage(40, Gdx.graphics.getHeight()));
		
		menuGui.getGroup().setPosition(player.getX()-whatValueIsPercentage(25, Gdx.graphics.getWidth()), player.getY()- whatValueIsPercentage(34, Gdx.graphics.getHeight()));
		
			Boolean isPressedESC = Gdx.input.isKeyJustPressed(Keys.ESCAPE);
		    if(isPressedESC){
		    	menuGui.setClicked();
		    }
	}	
	
	private void stageUpdate() {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();	
	}
	
	
	public void update(Box2DDebugRenderer debugRenderer){
			// Mostrar Debug de hitboxes da box2d
			isPressedF1 = Gdx.input.isKeyJustPressed(Keys.F1);
			//Apenas para debug.
			if(showHitBoxes) {
				
				debugRenderer.render(player.getPlayerWorld(), player.getCam().combined);
				
				fpsTable.debugAll(); 
				
			}else {fpsTable.setDebug(false); fpsLabel.setDebug(false);}
			
			if(isPressedF1) {
				showHitBoxes = !showHitBoxes;
				positionLabel.setVisible(!positionLabel.isVisible());
			}		
		}
	
	private float whatValueIsPercentage(float percentage, float maxValue100)
	{
		return (percentage * maxValue100) / 100;
	}
	
	public void dispose(){
		font.dispose();
		stage.dispose();
		menuGui.dispose();
	}
	
}