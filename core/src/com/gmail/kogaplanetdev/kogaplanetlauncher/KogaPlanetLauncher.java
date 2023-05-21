package com.gmail.kogaplanetdev.kogaplanetlauncher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.MapDrawer;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.Player;
import com.gmail.kogaplanetdev.kogaplanetlauncher.ui.PlayerGui;


public class KogaPlanetLauncher extends ApplicationAdapter {
	
	// entidades
	private Player player;
	
	// Mover para uma classe de entidade
	Rectangle rectangle;
	FPSLogger fpsLogger;
	
	// Assets
	Texture logoKGP;
	TextureAtlas idleJames, walkingJames;
	SpriteBatch entitiesBatch;
    Sprite KogaSprite;
	PlayerGui gui;
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
		
		// Cria��o do mundo
		WORLD = new World(new Vector2(0 , 0), true);	
		
		// S� para usar como efeito de compara��o, inutil, mas legal deixar
		fpsLogger = new FPSLogger();
		
		// Gr�ficos
		entitiesBatch = new SpriteBatch();
		idleJames = new TextureAtlas("sprites/james/james_idle.atlas");	
		walkingJames = new TextureAtlas("sprites/james/james_walking.atlas");
		
		logoKGP = new Texture("logos/256x_kgp.png");
		KogaSprite = new Sprite(logoKGP);
		
		// Sistema de tiles
		mapDrawer = new MapDrawer(entitiesBatch);
		mapDrawer.loadMap();
	
		// passe o nome de cada sprite armazenado no atlas ao player(4 no total)
		player = new Player(entitiesBatch, idleJames, walkingJames);
		String SpritesNames[] = {"Idle_back","Idle_front","Idle_right","Idle_left"};
		player.create(mapDrawer.getOriginPosition(), SpritesNames[1]);		
		
		// Boas pr�ticas em locais errados.java 
		for(int count = 0; count < SpritesNames.length ; count++){
		player.setAtlasSprites(count, SpritesNames[count]);
		}
		
		// Classe de User interface:)
		gui = new PlayerGui(entitiesBatch, player);

		// debug
		debugRenderer = new Box2DDebugRenderer();

		
		
		WORLD.setContactListener(new com.gmail.kogaplanetdev.kogaplanetlauncher.entities.CollisionHandler());
	}

	@Override
	public void render () {
	
		WORLD.step(Gdx.graphics.getDeltaTime(), 8, 3);
			
		//Utilidades para debug
		ScreenUtils.clear(Color.SLATE);
		fpsLogger.log();
		
		//Renderiza cada tile do mapa
		mapDrawer.renderMap(entitiesBatch);
		
		// O player come�a a sua pr�pria instancia de renderiza��o.
		player.update();
		
		// update de UI
		gui.update(entitiesBatch);
		gui.update(debugRenderer);
		
		
		
	}
	@Override
	public void dispose () {
		entitiesBatch.dispose();
		gui.dispose();
	}
}
