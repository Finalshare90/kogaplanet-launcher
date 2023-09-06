package com.gmail.kogaplanetdev.kogaplanetlauncher;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.MapDrawer;
import com.gmail.kogaplanetdev.kogaplanetlauncher.entities.Player;
import com.gmail.kogaplanetdev.kogaplanetlauncher.ui.PlayerGui;


public class KogaPlanetLauncher extends ApplicationAdapter {
	
	//Values
	
	// Entities
	private Player player;
	
	// Mover para uma classe de entidade
	private FPSLogger fpsLogger;
	
	// Assets
	private TextureAtlas idleJames, walkingJames;
	private SpriteBatch entitiesBatch;
	private PlayerGui gui;
	private MapDrawer mapDrawer;
	
    // Physics go brrr haha
	private final World WORLD = new World(new Vector2(0 , 0), true);;   
    private Box2DDebugRenderer debugRenderer;
   
	@Override
	public void create () {
		
		// The physic simulation world.	
		
		fpsLogger = new FPSLogger();
		
		// Assets
		entitiesBatch = new SpriteBatch();
		idleJames = new TextureAtlas("sprites/james/james_idle.atlas");	
		walkingJames = new TextureAtlas("sprites/james/james_walking.atlas");
				
		// Tile System
		mapDrawer = new MapDrawer(WORLD);
		mapDrawer.loadMap();
	
		// TODO: do a better way of loading graphics into the player.
		player = new Player(idleJames, walkingJames, WORLD, mapDrawer.getMapHeight(), mapDrawer.getMapWidth());
		String SpritesNames[] = {"Idle_back","Idle_front","Idle_right","Idle_left"};
		player.create(mapDrawer.getOriginPosition(), SpritesNames[1]);		
		
		// what the fuck is this?
		for(int count = 0; count < SpritesNames.length ; count++){
			player.setAtlasSprites(count, SpritesNames[count]);
		}
		
		// User GUI:)
		gui = new PlayerGui(entitiesBatch, player);

		// debug
		debugRenderer = new Box2DDebugRenderer();

		
		
		WORLD.setContactListener(new com.gmail.kogaplanetdev.kogaplanetlauncher.entities.CollisionHandler());
	}

	@Override
	public void render () {
	
		// Simulate the world physics
		WORLD.step(Gdx.graphics.getDeltaTime(), 8, 3);
			
		// Debug utils:)
		ScreenUtils.clear(Color.SLATE);
		fpsLogger.log();
		
		// Render the map tiles
		mapDrawer.renderMap(entitiesBatch);
		
		// Player starts his own render
		player.update(entitiesBatch);
		
		// UI update
		gui.update(entitiesBatch);
		gui.update(debugRenderer);
	}
	@Override
	public void dispose () {
		entitiesBatch.dispose();
		gui.dispose();
		player.dispose();
		WORLD.dispose();
		mapDrawer.dispose();
	}
}
