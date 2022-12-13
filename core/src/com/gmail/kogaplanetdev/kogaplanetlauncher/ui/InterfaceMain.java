package com.gmail.kogaplanetdev.kogaplanetlauncher.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.gmail.kogaplanetdev.kogaplanetlauncher.KogaPlanetLauncher;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.Player;


public class InterfaceMain{

	private SpriteBatch batch;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;
	private Player player;
	private Boolean isPressedF1, showHitBoxes = false;
	
	Stage stage;
	Table table;
	
	
	public InterfaceMain(SpriteBatch batch, Player player) {	
		
		this.batch = batch;
		this.player = player;
		
		// Parte do sistema de hitbox, debug apenas
		showHitBoxes = false;
		
		// Gera a fonte PrStart, não é essencial, mas vale a pena usar =)
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PrStart.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 12;
	    font = generator.generateFont(parameter);
	    
	    stage = new Stage(this.player.viewport, batch);
	    Gdx.input.setInputProcessor(stage);
	    table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);
	}
	
	
	public void update() {
		int fps = Gdx.graphics.getFramesPerSecond();
		String fpsDisplay = "FPS: " + Integer.toString(fps);	
		font.draw(batch,fpsDisplay, player.getX() + 560, player.getY() + 345);
	}	
	
	public void createWidgetComponents() {
		
		// Inutil por enquanto:(
		
	}
	
	public void stageUpdate() {
		
		// Inutil por enquanto:(
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
	}
	
	public void update(Box2DDebugRenderer debugRenderer){
			// Mostrar Debug de hitboxes da box2d
			isPressedF1 = Gdx.input.isKeyJustPressed(Keys.F1);
		
			//Apenas para debug.
			if(showHitBoxes) {
			 debugRenderer.render(KogaPlanetLauncher.WORLD, player.getCam().combined);
			 table.debug(Debug.all);
			}
			
			if(isPressedF1) {
				showHitBoxes = !showHitBoxes;
				table.debug(Debug.none); 	
			}
		}
	
	public void dispose(){
		font.dispose();
		stage.dispose();
	}
	
}
