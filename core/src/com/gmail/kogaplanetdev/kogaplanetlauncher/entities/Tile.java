package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tile {
	private boolean isCollidable = false;
	private boolean canKill = false;
	private int x, y;
	private Texture texture;
	private BodyDef bodyDef;
	private Body body;
	private PolygonShape polygonShape;
	private FixtureDef fixtureDef;
	private Fixture fixture;
	private World WORLD;
	
	public Tile(int x, int y, World WORLD){
		this.x = x;
		this.y = y;
		this.WORLD = WORLD;
		texture = new Texture("misc/blank.png");
	}

	private HashMap<String, Object> setFixtureData(){
		HashMap<String, Object> fixtureData = new HashMap<>();
		fixtureData.put("canKill", canKill);
		return fixtureData;
	}
	
	public void createBody(){
		if(isCollidable){
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(x + 32, y + 32);
			body = WORLD.createBody(bodyDef);
			polygonShape = new PolygonShape();
			polygonShape.setAsBox(texture.getWidth()/2, texture.getHeight()/2);
			fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixture = body.createFixture(fixtureDef);
			fixture.setUserData(setFixtureData());
		}
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	public void setTileTexture(Texture texture)
	{
		this.texture =  texture;
	}
	
	public Texture getTileTexture()
	{
		return texture;
	}
	
	public void setIsCollidable(boolean isCollidable)
	{
		this.isCollidable = isCollidable;
	}
}
