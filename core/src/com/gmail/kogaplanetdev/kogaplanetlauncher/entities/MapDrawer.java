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
	
	
	public String dir = System.getProperty("user.home") + File.separator + "Documents" +
	File.separator + "KogaPlanetLauncher"+ File.separator + "games";
	Reader reader;
	
	public MapDrawer(SpriteBatch batch){
		this.batch = batch;
	}
	
	
	public void verify() {
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
	
	public void scan(){
		this.reader = new Reader(dir + File.separator + "currentMap");
		try { reader.scan();	
		} catch (Exception e) {e.printStackTrace();}
		
		tileTexture = new ArrayList<Texture>();
		for (int c = 0; c < reader.getTiles().size(); c++) {
		 tileTexture.add(new Texture(dir + File.separator + reader.getTiles().get(c).getTexture()));		
		}
		reader.tests();
	}
	
	public void calcTilePosition(){			
		
		ArrayList<Object> map = reader.getMap();
		
		int mapPosition = 0;
		int currentTileX = 0;
		int currentTileY = 0;
		
		for(int count = 0; count < map.size(); count++){
			
		System.out.println("tamanho do mapa: " + map.size());
		System.out.println("tile atual: " + map.get(count));
			if(!map.get(mapPosition).equals(";")){
				
				tiles.add(new Tile());
				System.out.println("tile size: " + tiles.size());
				tiles.get(mapPosition).y = currentTileY;
 				tiles.get(mapPosition).x = currentTileX + tileTexture.get(0).getWidth();
				currentTileX = tiles.get(mapPosition).x;
				System.out.println("tile pos: " + tiles.get(mapPosition).y + " " + tiles.get(mapPosition).x + "\n");
				mapPosition++;
				}else{
				currentTileX = 0;
				currentTileY = currentTileY - tileTexture.get(0).getHeight();
				map.remove(mapPosition);
			}			
			System.out.println("count: " + count);
			System.out.println("mapPos: " + mapPosition + "\n");
		}
		/*
		for(int e = 0; tiles.size() > e; e++){
				System.out.println(tiles.get(e).x +" " + tiles.get(e).y);
				System.out.println(map.get(e));
			}
		*/
	}
	
	public void loadBodies() {
		
		ArrayList<Object> map = reader.getMap();
		
		
		System.out.println(tiles.size());
		for(int mapSymbol = 0; mapSymbol < tiles.size(); mapSymbol++){
			int currentSymbol = Integer.parseInt((String)map.get(mapSymbol));		
			tiles.get(mapSymbol).texture = tileTexture.get(currentSymbol);
				tiles.get(currentSymbol).isCollidable = reader.getTiles().get(currentSymbol).isCollidable();
				System.out.println(currentSymbol + " " + tiles.get(currentSymbol).isCollidable);
		}
	}
	
	public void render(){
		for(int count = 0; count < tiles.size(); count++){
			batch.draw(tiles.get(count).texture, tiles.get(count).x, tiles.get(count).y);
		}
	}
}
	

