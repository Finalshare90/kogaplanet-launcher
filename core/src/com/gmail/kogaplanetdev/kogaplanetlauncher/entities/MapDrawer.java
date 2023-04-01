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
		System.out.println("default directory has already been created, proceeding...");
			}
			else {
			System.out.println("default directory does not exist, creating new default directory.");
			file.mkdirs();
		}
	}
	
	public void loadMap() {
		try {
		handler = new TagHandler();
		handler.parserInit(new Parser(dir + File.separator + "currentMap.3ml"));
		
	
		defineSpawnpoint();
		TagUtil.printTag(handler.call("spawnpoint"));
		
		texturesFactory();
		TagUtil.printTag(handler.call("symbol"));
		
		tileFactory();
		loadBodies();
		
		System.out.println(tiles.get(1).isCollidable);
		
		loadTextures();
		}catch (Exception e) {e.printStackTrace();}
	}
	
	private void defineSpawnpoint(){
		originPosition.x = Integer.parseInt(handler.call("spawnpoint").data.get(0));
		originPosition.y = Integer.parseInt(handler.call("spawnpoint").data.get(1));	
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
		System.out.println(tiles.size());

		for(int mapSymbol = 0; mapSymbol < tiles.size(); mapSymbol++){
			if(!map.get(mapSymbol).equals(";")){
			tiles.get(mapSymbol).texture = tileTexture.get(
					Integer.parseInt((String)map.get(mapSymbol)));
				}else{
					tiles.get(mapSymbol).texture = blank;
				}
			}
		}
	
	private void loadBodies() {
		
		ArrayList<String> map = handler.call("map").data;
		ArrayList<String> collidableTag = handler.call("collidable").data;
		
		for(int mapSymbol = 0; mapSymbol < tiles.size(); mapSymbol++){
			if(!map.get(mapSymbol).equals(";")){
			
			int currentSymbol = Integer.parseInt(map.get(mapSymbol));		
			
			
			if(collidableTag.contains(map.get(mapSymbol))) {
				tiles.get(mapSymbol).isCollidable = true;
			}else{
				tiles.get(mapSymbol).isCollidable = false;
			}
						
			tiles.get(mapSymbol).texture = tileTexture.get(currentSymbol);
			tiles.get(mapSymbol).createBody();
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
	

