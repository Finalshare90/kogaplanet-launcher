package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gmail.kogaplanetdev.kogaplanetlauncher.KogaPlanetLauncher;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Tile {
	boolean isCollidable = false;
	int x, y;
	Texture texture;
	BodyDef bodyDef;
	Body body;
	PolygonShape polygonShape;
	FixtureDef fixtureDef;
	Fixture fixture;
	
	public static int SCALLING = 2; 
	
	
	public Tile(){
		x = 0;
		y = 0;
	}

	
	public void createBody(){
		if(isCollidable == true){
			bodyDef = new BodyDef();
			bodyDef.type = BodyType.StaticBody;
			bodyDef.position.set(x + 32, y + 32);
			body = KogaPlanetLauncher.WORLD.createBody(bodyDef);
			polygonShape = new PolygonShape();
			polygonShape.setAsBox(texture.getWidth()/2, texture.getHeight()/2);
			fixtureDef = new FixtureDef();
			fixtureDef.shape = polygonShape;
			fixture = body.createFixture(fixtureDef);
			
		}
	}
	
}
