package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import finalshare.tileReader.essentials.Reader;

public class MapDrawer {

	SpriteBatch batch;
	ArrayList<Texture> tileTexture;
	ArrayList<Tile> tiles = new ArrayList<Tile>();	
	String textures;
	Texture blank = new Texture("misc/blank.png");
	Vector2 originPosition = new Vector2();
	
	
	/*
	 Se o sistema de tile começar a quebrar, comentar código acima e o a baixo e substituir por esse
	 VVVV
	 public String dir = System.getProperty("user.home") + File.separator + "Documents" +
	 File.separator + "KogaPlanetLauncher"+ File.separator + "games";
	 */
	
	// Temporário? vai saber, você sabe? eu não sei, pq eu saberia?
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
		scanFile();
		tileFactory();
		loadBodies();
		loadTextures();
	}
	
	private void scanFile(){
		
		// Cria a instância do leitor e lê as texturas e os "bits" de cada tile no 3ml.
		this.reader = new Reader(dir + File.separator + "currentMap");
		
		
		try {
			reader.scan();	
		} catch (Exception e) {e.printStackTrace();}
		
		originPosition.x = reader.getOriginPosition()[0];
		originPosition.y = reader.getOriginPosition()[1];
		
		// Gera o path de cada textura
		texturesFactory();
	}
	
	private void texturesFactory(){
		tileTexture = new ArrayList<Texture>();
		for (int c = 0; c < reader.getTiles().size(); c++) {
		 tileTexture.add(new Texture(dir + File.separator + reader.getTiles().get(c).getTexture()));		
		}
	}
	
	
	
	private void tileFactory(){			
		
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
	
	private void loadTextures() {

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
	
	private void loadBodies() {
		
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
	

