package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import finalshare.tileReader.essentials.Reader;

import com.kogaplanet.lunarlatteMarkupLanguage.Parser;
import com.kogaplanet.lunarlatteMarkupLanguage.TagNode;
import com.kogaplanet.lunarlatteMarkupLanguage.api.*;

public class MapDrawer {

	SpriteBatch batch;
	ArrayList<Texture> tileTexture;
	ArrayList<List<Tile>> tileMap = new ArrayList<List<Tile>>();	
	String textures;
	Texture blank = new Texture("misc/blank.png");
	Vector2 originPosition = new Vector2();
	TagHandler handler;
	TagNode mapTag;
	
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
	
	public MapDrawer(){
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
		}catch (Exception e) {e.printStackTrace();}
		
		
		mapTag = handler.call("map");
		HashMap<String, Layer> map = getLayers(mapTag);
		
		
		defineSpawnpoint();
		
		texturesFactory();
		
		// Produces each tile instance, not the body or his texture,
		// just both the instance and his position. 
		calculateTilePos(map, mapTag);
		
		// i'm too lazy to handle it:)
		try {
			
			// Create *some* tiles physics by receiving the current tile layer and his raw tag data.
			loadBodies(map, tileMap, mapTag);
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		loadTextures(map, mapTag);
	
	}
	
	private void defineSpawnpoint(){	
		
		try {
			originPosition.x = Integer.parseInt(handler.call("spawnpoint").data.get(0));
			originPosition.y = Integer.parseInt(handler.call("spawnpoint").data.get(1));
		
		}catch (NullPointerException e) {
			System.err.println("spawnpoint not found, setting spawnpoint to 0,0");
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
	
	
	
	
	// Creates the instance of a specific tile layer and define his position.
 	private void calculateTilePos(HashMap<String, Layer> map, TagNode mapTag){			
		
 		
 		for(int c = 0; c < map.size(); c++){
 		
 			Layer layerData = map.get(mapTag.data.get(c));
 			ArrayList<String> layerMap = layerData.tileMap;
 			
 		
 			int currentTileX = 0;
 			int currentTileY = 0;
		
 			//Create tiles objects and process their positions
 			tileMap.add(new ArrayList<Tile>());
 			
 			for(int tile = 0; tile < layerMap.size(); tile++){
 				if(!layerMap.get(tile).equals(";")){			
 					tileMap.get(c).add(new Tile(currentTileX + tileTexture.get(0).getWidth(), currentTileY));
 					currentTileX = tileMap.get(c).get(tile).x;
 				}
			
 				else{
 					layerMap.remove(tile);
 					currentTileX = 0;
 					currentTileY = currentTileY - tileTexture.get(0).getHeight();
 					tile--;
				}
			}
 		}
	}
 	
	
	private void loadTextures(HashMap<String, Layer> map, TagNode mapTag) {

			
		for(int layer = 0; layer < mapTag.data.size(); layer++) {
			Layer layerData = map.get(mapTag.data.get(layer));
			ArrayList<String> layerMap = layerData.tileMap;
		
			for(int mapSymbol = 0; mapSymbol < tileMap.get(layer).size(); mapSymbol++){
				if(!layerMap.get(mapSymbol).equals(";") && !layerMap.get(mapSymbol).equals("B")){
					// creates and put the textures in-order
					tileMap.get(layer).get(mapSymbol).texture = tileTexture.get(Integer.parseInt(layerMap.get(mapSymbol)));					
					}
				}
			}
		}
	
	
	
	private void loadBodies(HashMap<String, Layer> map, ArrayList<List<Tile>> tileMap, TagNode mapData) {
		
		
		// try to use a 3ml API call there.
		//ArrayList<String> map = mapData.data;		
		ArrayList<String> collidableTag = handler.call("collidable").data;
		
		
		for(int layer = 0; layer < tileMap.size(); layer++) {
			
			Layer layerData = map.get(mapTag.data.get(layer));
			ArrayList<String> layerMap = layerData.tileMap;
			
			for(int tile = 0; tile < tileMap.get(layer).size(); tile++){
				if(!layerMap.get(tile).equals(";") && !layerMap.get(tile).equals("B")){
			
					// Current tile symbol, the little number in the map, not the map position. 
					int currentSymbol = Integer.parseInt(layerMap.get(tile));
			
					// Checks if the symbol it is inside "collidable" Tag
					if(collidableTag.contains(layerMap.get(tile))) {
						tileMap.get(layer).get(tile).isCollidable = true;
					}else{
						tileMap.get(layer).get(tile).isCollidable = false;
					}
			
					// Put the textures, bodies and the tiles together
					tileMap.get(layer).get(tile).texture = tileTexture.get(currentSymbol);
					tileMap.get(layer).get(tile).createBody();
				}
			}
		}
	}
	
	private HashMap<String, Layer> getLayers(TagNode mapTag) {
	
		HashMap<String, Layer> layers = new HashMap<>();
		
		for(int currentLayer = 0; currentLayer < mapTag.data.size(); currentLayer++){
			
			String layerName = mapTag.data.get(currentLayer);
			TagNode layerTag = handler.call(layerName);
			
			
			layers.put(layerName, new Layer(layerTag.data, currentLayer));
		}
		
		return layers;
	}
	
	public void renderMap(SpriteBatch batch){
		batch.begin();
		
		for(int layer = 0; layer < tileMap.size(); layer++) {
			for(int tile = 0; tile < tileMap.get(layer).size(); tile++){
				batch.draw(tileMap.get(layer).get(tile).texture, tileMap.get(layer).get(tile).x, tileMap.get(layer).get(tile).y);
			}
		}
		batch.end();
	}
	
	public Vector2 getOriginPosition() {
		return originPosition;
	}
	
	public void dispose(){
		for(int layer = 0; layer < tileMap.size(); layer++) {
			for(int tile = 0; tile < tileMap.get(layer).size(); tile++){
				tileMap.get(layer).get(tile).texture.dispose();;
			}
		}
	}
}
	

