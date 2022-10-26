package com.gmail.kogaplanetdev.kogaplanetlauncher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.*;
import com.gmail.kogaplanetdev.kogaplanetlauncher.ui.InterfaceMain;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class KogaPlanetLauncher extends ApplicationAdapter {
	
	// entidades
	Player p1;
	
	// Mover para uma classe de entidade
	Rectangle rectangle;
	FPSLogger fpsLogger;
	
	// Assets
	Texture logoKGP;
	TextureAtlas atlas;
	SpriteBatch entitiesBatch;
    Sprite KogaSprite;
	InterfaceMain ui;
	MapDrawer mapDrawer;
	
    // Physics go brrr haha
    public static World WORLD;
    BodyDef bodyDef;  
    PolygonShape poly;
    Body body;
    FixtureDef fixtureDef;
    Fixture fixture;
    Box2DDebugRenderer debugRenderer;
   
	@Override
	public void create () {
			
		// Só para usar como efeito de comparação, inutil, mas legal deixar
		fpsLogger = new FPSLogger();
		
		// Gráficos
		entitiesBatch = new SpriteBatch();
		atlas = new TextureAtlas("sprites/james/james_IDLE.atlas");	
		logoKGP = new Texture("logos/256x_kgp.png");
		KogaSprite = new Sprite(logoKGP);
		
	
		// passe o nome de cada sprite armazenado no atlas ao player(4 no total)
		p1 = new Player(entitiesBatch, atlas);
		String SpritesNames[] = {"Idle_Costas","Idle_Frente","Idle_Direita","Idle_Esquerda"};
		p1.create(200, 200, SpritesNames[1]);
		// Boas práticas em locais errados.java 
		for(int count = 0; count < SpritesNames.length ; count++){
		p1.setAtlasSprites(count, SpritesNames[count]);
		}
		// Classe de User interface:)
		ui = new InterfaceMain(entitiesBatch, p1);
		ui.createWidgetComponents();
		
		// debug
		debugRenderer = new Box2DDebugRenderer();
		
		// Criação do mundo
		WORLD = new World(new Vector2(0 , 0), true);		
		
		// cria corpo do player.
		p1.createBody();
		
		// Sistema de tiles
		mapDrawer = new MapDrawer(entitiesBatch);
		mapDrawer.verify();
		mapDrawer.scan();
		mapDrawer.calcTilePosition();
		mapDrawer.loadBodies();
		mapDrawer.loadTextures();
		
		
	}

	@Override
	public void render () {
	
		WORLD.step(Gdx.graphics.getDeltaTime(), 8, 3);
			
		//Utilidades para debug ou simplesmente para testes
		ScreenUtils.clear(Color.SLATE);
		fpsLogger.log();
		
		entitiesBatch.begin();
		
		// Desenha a logo do KGP
		mapDrawer.render();
		ui.update();
		
		entitiesBatch.end();
		
		//Após o fim do Batch acima, o player começa a sua própria instancia de renderização.
		p1.update();
		
		
		// update de UI para debug, ele não precisa estar em um fluxo Batch.
		ui.update(debugRenderer);
		ui.stageUpdate();
	}
	@Override
	public void dispose () {
		entitiesBatch.dispose();
		poly.dispose();
		ui.dispose();
	}
}
