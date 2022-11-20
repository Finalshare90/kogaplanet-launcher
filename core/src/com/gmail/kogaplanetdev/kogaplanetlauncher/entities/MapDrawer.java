package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.io.File;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import finalshare.tileReader.essentials.Reader;

public class MapDrawer {

	SpriteBatch batch;
	ArrayList<Texture> tileTexture;
	ArrayList<Tile> tiles = new ArrayList<Tile>();	
	String textures;
	Texture blank = new Texture("misc/blank.png");
	
	public String dir = System.getProperty("user.home") + File.separator + "Documents" +
	File.separator + "KogaPlanetLauncher"+ File.separator + "games";
	Reader reader;
	
	public MapDrawer(SpriteBatch batch){
		this.batch = batch;
		verifyDirectory();
	}
	
	
	private void verifyDirectory() {
	System.out.println(dir);
		File file = new File(dir);
		if(file.exists()) {
		System.out.println("default directory has already been created, proceeding...");
			}
			else {
			System.out.println("default directory does not exist, creating new default directory.");
			file.mkdirs();
		}
	}
	
	public void scanFile(){
		
		// Cria a instância do leitor e lê as texturas e os "bits" de cada tile no 3ml.
		this.reader = new Reader(dir + File.separator + "currentMap");
		
		try {
			reader.scan();	
		} catch (Exception e) {e.printStackTrace();}
		
		// Gera o path de cada textura
		texturesFactory();
	}
	
	private void texturesFactory(){
		tileTexture = new ArrayList<Texture>();
		for (int c = 0; c < reader.getTiles().size(); c++) {
		 tileTexture.add(new Texture(dir + File.separator + reader.getTiles().get(c).getTexture()));		
		}
	}
	
	public void tileFactory(){			
		
		ArrayList<Object> map = reader.getMap();
		
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
	
	public void loadTextures() {

		ArrayList<Object> map = reader.getMap();
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
	
	public void loadBodies() {
		
		ArrayList<Object> map = reader.getMap();
		
		System.out.println(tiles.size());
		for(int mapSymbol = 0; mapSymbol < tiles.size(); mapSymbol++){
			if(!map.get(mapSymbol).equals(";")){
			int currentSymbol = Integer.parseInt((String)map.get(mapSymbol));		
			tiles.get(mapSymbol).texture = tileTexture.get(currentSymbol);
				tiles.get(mapSymbol).isCollidable = reader.getTiles().get(currentSymbol).isCollidable();
				System.out.println(currentSymbol + " " + tiles.get(currentSymbol).isCollidable);
				tiles.get(mapSymbol).createBody();
			}
		}
	}
	
	public void render(){
		for(int count = 0; count < tiles.size(); count++){
			batch.draw(tiles.get(count).texture, tiles.get(count).x, tiles.get(count).y);
		}
	}
}
	

