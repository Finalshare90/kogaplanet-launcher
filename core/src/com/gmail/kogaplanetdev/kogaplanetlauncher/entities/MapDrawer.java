package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import finalshare.tileReader.essentials.Reader;

import com.kogaplanet.lunarlatteMarkupLanguage.Parser;
import com.kogaplanet.lunarlatteMarkupLanguage.TagNode;
import com.kogaplanet.lunarlatteMarkupLanguage.api.*;
import com.kogaplanet.lunarlatteMarkupLanguage.util.TagUtil;

public class MapDrawer {

	SpriteBatch batch;
	ArrayList<Texture> tileTexture;
	ArrayList<Tile> tiles = new ArrayList<Tile>();	
	String textures;
	Texture blank = new Texture("misc/blank.png");
	Vector2 originPosition = new Vector2();
	TagHandler handler;
	
	/*
	 small "if the sh*t goes wrong, break the panel" thing.
	 
	 public String dir = System.getProperty("user.home") + File.separator + "Documents" +
	 File.separator + "KogaPlanetLauncher"+ File.separator + "games";
	 */
	
	// gambiarra, don't ask me what it means.
	static private String mapDialog() {
		JOptionPane setMapPane = new JOptionPane();
		JDialog dialog = setMapPane.createDialog("Insert the name of your map");
		dialog.setAlwaysOnTop(true);
		setMapPane.setName("Insert your map");
		setMapPane.setToolTipText("Insert your map name");
		mapName = setMapPane.showInputDialog(dialog, "please, enter the name of the desired map");
		dialog.dispose();
		return mapName;
	}
	public static String mapName = mapDialog();
	
	
	public String dir = System.getProperty("user.home") + File.separator + "Documents" +
	File.separator + "KogaPlanetLauncher"+ File.separator + "games" + File.separator + mapName;
		
	Reader reader;
	
	public MapDrawer(SpriteBatch batch){
		this.batch = batch;
		verifyDirectory();
	}
	
	
	private void verifyDirectory() {
		
		File file = new File(dir);
		if(file.exists()) {
		System.err.println("default directory has already been created, proceeding...");
			}
			else {
			System.err.println("default directory does not exist, creating new default directory.");
			file.mkdirs();
		}
	}
	
	public void loadMap() {
		try {
		handler = new TagHandler();
		handler.parserInit(new Parser(dir + File.separator + "currentMap.3ml"));
		
		defineSpawnpoint();		
		texturesFactory();
		tileFactory();
		loadBodies();
		loadTextures();
		}catch (Exception e) {e.printStackTrace();}
	}
	
	private void defineSpawnpoint(){
		try {
			originPosition.x = Integer.parseInt(handler.call("spawnpoint").data.get(0));
			originPosition.y = Integer.parseInt(handler.call("spawnpoint").data.get(1));
		}catch (NullPointerException e) {
			e.printStackTrace();
			originPosition.x = 0;
			originPosition.y = 0;
		}
	}
	
	private void texturesFactory(){
		
		// Texture class with the path's
		tileTexture = new ArrayList<Texture>();
		
		// Tag containing the path's to insert inside of tileTexture
		TagNode tiles = handler.call("symbol");
		
		// Put the data of a tag together inside of tileTexture
		for (int c = 0; c < tiles.data.size(); c++) {
		 tileTexture.add(new Texture(dir + File.separator + tiles.data.get(c)));		
		}
	}
	
	
	
	private void tileFactory(){			
		
		ArrayList<String> map = handler.call("map").data;
		
		int currentTileX = 0;
		int currentTileY = 0;
		
		//Create tiles objects and process their positions
		for(int count = 0; count < map.size(); count++){
			if(!map.get(count).equals(";")){			
				tiles.add(new Tile());
				tiles.get(count).y = currentTileY;
 				tiles.get(count).x = currentTileX + tileTexture.get(0).getWidth();
				currentTileX = tiles.get(count).x;
				}else{
				tiles.add(new Tile());
				tiles.get(count).y = currentTileY;
 				tiles.get(count).x = currentTileX + tileTexture.get(0).getWidth();
				currentTileX = 0;
				currentTileY = currentTileY - tileTexture.get(0).getHeight();
				}
		}
		}
	
	private void loadTextures() {

		ArrayList<String> map = handler.call("map").data;

		
		for(int mapSymbol = 0; mapSymbol < tiles.size(); mapSymbol++){
			if(!map.get(mapSymbol).equals(";")){
			
			// It will create and put the textures in-order
			tiles.get(mapSymbol).texture = tileTexture.get(
					Integer.parseInt((String)map.get(mapSymbol)));
				}else{
					// Shhh, little Gambiarra here.
					tiles.get(mapSymbol).texture = blank;
				}
			}
		}
	
	private void loadBodies() {
		
		// 3ml API call both tags inside of the parser.
		ArrayList<String> map = handler.call("map").data;
		ArrayList<String> collidableTag = handler.call("collidable").data;
		
		
		for(int currentMapPosition = 0; currentMapPosition < tiles.size(); currentMapPosition++){
			if(!map.get(currentMapPosition).equals(";")){
			
			// Current tile symbol, the little number in the map, not the map position. 
			int currentSymbol = Integer.parseInt(map.get(currentMapPosition));
			
			// Checks if the symbol it is inside "collidable" Tag
			if(collidableTag.contains(map.get(currentMapPosition))) {
				tiles.get(currentMapPosition).isCollidable = true;
			}else{
				tiles.get(currentMapPosition).isCollidable = false;
			}
			
			// Put the textures, bodies and the tiles together
			tiles.get(currentMapPosition).texture = tileTexture.get(currentSymbol);
			tiles.get(currentMapPosition).createBody();
			}
		}
	}
	
	public void renderMap(SpriteBatch batch){
		batch.begin();
		for(int count = 0; count < tiles.size(); count++){
			batch.draw(tiles.get(count).texture, tiles.get(count).x, tiles.get(count).y);
		}
		batch.end();
	}
	
	public Vector2 getOriginPosition() {
		return originPosition;
	}
	
}
	

